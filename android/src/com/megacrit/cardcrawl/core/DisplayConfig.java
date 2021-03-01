package com.megacrit.cardcrawl.core;

import android.os.Environment;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.android.SpireAndroidLogger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Scanner;

public class DisplayConfig {
    private static final SpireAndroidLogger logger = SpireAndroidLogger.getLogger(DisplayConfig.class);
    private static final String DISPLAY_CONFIG_LOC = "info.displayconfig";
    private static final int DEFAULT_W = 1280;
    private static final int DEFAULT_H = 720;
    private static final int DEFAULT_FPS_LIMIT = 60;
    private static final boolean DEFAULT_FS = false;
    private static final boolean DEFAULT_WFS = false;
    private static final boolean DEFAULT_VSYNC = true;
    private int width;
    private int height;
    private int fps_limit;
    private boolean isFullscreen;
    private boolean wfs;
    private boolean vsync;

    private DisplayConfig(int width, int height, int fps_limit, boolean isFullscreen, boolean wfs, boolean vsync) {
        this.width = width;
        this.height = height;
        this.fps_limit = fps_limit;
        this.isFullscreen = isFullscreen;
        this.wfs = wfs;
        this.vsync = vsync;
    }

    public String toString() {
        HashMap<String, Object> hm = new HashMap();
        hm.put("width", this.width);
        hm.put("height", this.height);
        hm.put("fps_limit", this.fps_limit);
        hm.put("isFullscreen", this.isFullscreen);
        hm.put("wfs", this.wfs);
        hm.put("vsync", this.vsync);
        return hm.toString();
    }

    public static DisplayConfig readConfig() {
        logger.info("Reading info.displayconfig");
        ArrayList<String> configLines = readDisplayConfFile();
        createNewConfig();
        if (configLines.size() < 4) {
            return readConfig();
        } else if (configLines.size() == 5) {
            appendFpsLimit(configLines);
            return readConfig();
        } else {
            DisplayConfig dc;
            try {
                dc = new DisplayConfig(Integer.parseInt((configLines.get(0)).trim()), Integer.parseInt((configLines.get(1)).trim()), Integer.parseInt((configLines.get(2)).trim()), Boolean.parseBoolean((configLines.get(3)).trim()), Boolean.parseBoolean((configLines.get(4)).trim()), Boolean.parseBoolean((configLines.get(5)).trim()));
            } catch (Exception var3) {
                logger.info("Failed to parse the info.displayconfig going to recreate it with defaults.");
                createNewConfig();
                return readConfig();
            }

            logger.info("DisplayConfig successfully read.");
            return dc;
        }
    }

    private static ArrayList<String> readDisplayConfFile() {
        ArrayList<String> configLines = new ArrayList<>();

        ArrayList<String> ret;
        File extDir = Environment.getExternalStorageDirectory();
        try (Scanner s = new Scanner(new File(extDir, "info.displayconfig"))) {

            while (s.hasNextLine()) {
                configLines.add(s.nextLine());
            }

            return configLines;
        } catch (FileNotFoundException var7) {
            logger.info("File info.displayconfig not found, creating with defaults.");
            createNewConfig();
            ret = readDisplayConfFile();
        }

        return ret;
    }

    public static void writeDisplayConfigFile(int w, int h, int fps, boolean fs, boolean wfs, boolean vs) {
        PrintWriter writer = null;

        try {
            File extDir = Environment.getExternalStorageDirectory();
            writer = new PrintWriter(new File(extDir, "info.displayconfig"), "UTF-8");
            writer.println(Integer.toString(w));
            writer.println(Integer.toString(h));
            writer.println(Integer.toString(fps));
            writer.println(Boolean.toString(fs));
            writer.println(Boolean.toString(wfs));
            writer.println(Boolean.toString(vs));
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            logger.error("Exception caught:\n");
            e.printStackTrace();
            //ExceptionHandler.handleException(var11, logger);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    private static void createNewConfig() {
        logger.info("Creating new config with default values...");
        System.out.println(String.format(Locale.ENGLISH, "DisplayConfig:Width:%d, Height:%d", Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        writeDisplayConfigFile(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 60, false, false, false);
    }

    private static void appendFpsLimit(ArrayList<String> configLines) {
        logger.info("Updating config...");

        try {
            writeDisplayConfigFile(Integer.parseInt((configLines.get(0)).trim()), Integer.parseInt((configLines.get(1)).trim()), 60, Boolean.parseBoolean((configLines.get(2)).trim()), Boolean.parseBoolean((configLines.get(3)).trim()), true);
        } catch (Exception var2) {
            logger.info("Failed to parse the info.displayconfig going to recreate it with defaults.");
            createNewConfig();
        }

    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getMaxFPS() {
        return this.fps_limit;
    }

    public boolean getIsFullscreen() {
        return this.isFullscreen;
    }

    public boolean getWFS() {
        return this.wfs;
    }

    public boolean getIsVsync() {
        return this.vsync;
    }
}
