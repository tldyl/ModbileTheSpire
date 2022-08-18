package com.megacrit.cardcrawl.android.mods;

import com.google.gson.Gson;
import com.megacrit.cardcrawl.AndroidLauncher;
import com.megacrit.cardcrawl.android.SpireAndroidLogger;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class Loader {
    public static ModInfo[] MODINFOS;
    public static JarFile[] MOD_JARS;
    private static int loadedMods = 0;
    private static final SpireAndroidLogger logger = SpireAndroidLogger.getLogger(Loader.class);

    public static boolean isModLoaded(String modId) {
        for (ModInfo modinfo : MODINFOS) {
            if (modinfo.modId.equals(modId) && modinfo.isLoaded) return true;
        }
        return false;
    }

    public static int getLoadedMods() {
        return loadedMods;
    }

    public static void loadMods(String modFolderPath) {
        logger.info("Loading mods...");
        File modFolder = new File(modFolderPath);
        if (!modFolder.exists()) {
            modFolder.mkdir();
        }
        File[] files = modFolder.listFiles(pathname -> pathname.getAbsolutePath().endsWith(".jar"));
        MOD_JARS = new JarFile[files.length];
        MODINFOS = new ModInfo[files.length];
        Gson gson = new Gson();
        for (int i=0;i<files.length;i++) {
            try {
                MOD_JARS[i] = new JarFile(files[i]);
                ZipEntry entry = MOD_JARS[i].getEntry("ModTheSpire.json");
                if (entry != null) {
                    InputStream is = MOD_JARS[i].getInputStream(entry);
                    StringBuilder out = new StringBuilder();
                    byte[] b = new byte[4096];
                    for (int n; (n = is.read(b)) != -1;) {
                        out.append(new String(b, 0, n, StandardCharsets.UTF_8));
                    }
                    String modInfoString = out.toString();
                    MODINFOS[i] = gson.fromJson(modInfoString, ModInfo.class);
                } else {
                    String defaultModId = files[i].getName().replace(".jar", "");
                    logger.error("{} is missing ModTheSpire.json. Go yell at the author to fix it!", defaultModId);
                    ModInfo modInfo = new ModInfo();
                    modInfo.modId = defaultModId;
                    modInfo.modName = defaultModId;
                    modInfo.modVersion = "1.0";
                    modInfo.authorList = new String[]{"Anonymous"};
                    modInfo.dependencies = new String[0];
                    modInfo.descriptions = new HashMap<>();
                    modInfo.descriptions.put("eng", new String[]{modInfo.modId + " is missing ModTheSpire.json. Go yell at the author to fix it!"});
                    MODINFOS[i] = modInfo;
                }
                MODINFOS[i].jarURL = files[i].toURI().toURL();
                AndroidLauncher.instance.inject(files[i].getAbsolutePath());
                MODINFOS[i].isLoaded = true;
                logger.info("{} loaded.", MODINFOS[i].modId);
                loadedMods++;
            } catch (IOException e) {
                e.printStackTrace();
                CardCrawlGame.writeExceptionToFile(e);
                MODINFOS[i].isLoaded = false;
            }
        }
        logger.info("Successfully loaded {} mod(s).", Integer.toString(loadedMods));
        if (MOD_JARS.length > 0) Settings.isModded = true;
        regMods();
    }

    private static void regMods() {
        logger.info("Registering mods...");
        for (ModInfo info : MODINFOS) {
            if (!info.isLoaded) continue;
            String rootPackageName = info.modId;
            if (Character.isUpperCase(rootPackageName.charAt(0))) {
                char[] chars = rootPackageName.toCharArray();
                chars[0] = Character.toLowerCase(chars[0]);
                rootPackageName = new String(chars);
            }
            String modMainClassPath = rootPackageName + "." + info.modId;
            if (info.mainClassPath != null) {
                modMainClassPath = info.mainClassPath;
            }
            try {
                Class<?> clz = Class.forName(modMainClassPath);
                try {
                    Method initMethod = clz.getDeclaredMethod("initialize");
                    initMethod.invoke(null);
                } catch (NoSuchMethodException e) {
                    logger.error("Failed to call initialize() method when loading {}", info.modId);
                    logger.error("Method does not exist!");
                    info.isLoaded = false;
                    CardCrawlGame.writeExceptionToFile(e);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    logger.error("Failed to call initialize() method when loading {}", info.modId);
                    e.printStackTrace();
                    info.isLoaded = false;
                    CardCrawlGame.writeExceptionToFile(e);
                }
            } catch (ClassNotFoundException e) {
                logger.error("Unable to find main class: {} when registering {}", modMainClassPath, info.modId);
                info.isLoaded = false;
                CardCrawlGame.writeExceptionToFile(e);
            }
        }
        logger.info("Done.");
    }
}
