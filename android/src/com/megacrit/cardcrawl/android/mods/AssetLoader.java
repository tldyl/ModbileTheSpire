package com.megacrit.cardcrawl.android.mods;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.CardCrawlGame;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;

public class AssetLoader {
    public static Texture getTexture(String modId, String internalPath) {
        int i;
        for (i = 0; i < Loader.MODINFOS.length; i++) {
            if (Loader.MODINFOS[i].modId.equals(modId)) break;
        }
        if (i >= Loader.MODINFOS.length) return null;
        Texture ret = null;
        try {
            ZipEntry entry = Loader.MOD_JARS[i].getEntry(internalPath);
            if (entry != null) {
                InputStream is = Loader.MOD_JARS[i].getInputStream(entry);
                ret = new Texture(new FileHandle(internalPath) {
                    @Override
                    public InputStream read() {
                        return is;
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
            CardCrawlGame.writeExceptionToFile(e);
        }
        return ret;
    }

    public static FileHandle getFileHandle(String modId, String internalPath) {
        int i;
        for (i = 0; i < Loader.MODINFOS.length; i++) {
            if (Loader.MODINFOS[i].modId.equals(modId)) break;
        }
        if (i >= Loader.MODINFOS.length) return null;
        try {
            ZipEntry entry = Loader.MOD_JARS[i].getEntry(internalPath);
            if (entry != null) {
                InputStream is = Loader.MOD_JARS[i].getInputStream(entry);
                return new FileHandle(internalPath) {
                    @Override
                    public InputStream read() {
                        return is;
                    }
                };
            }
        } catch (IOException e) {
            e.printStackTrace();
            CardCrawlGame.writeExceptionToFile(e);
        }
        return null;
    }

    public static String getString(String modId, String internalPath) {
        int i;
        for (i = 0; i < Loader.MODINFOS.length; i++) {
            if (Loader.MODINFOS[i].modId.equals(modId)) break;
        }
        if (i >= Loader.MODINFOS.length) return null;
        String ret = null;
        try {
            ZipEntry entry = Loader.MOD_JARS[i].getEntry(internalPath);
            if (entry != null) {
                InputStream is = Loader.MOD_JARS[i].getInputStream(entry);
                InputStreamReader reader = new InputStreamReader(is);
                char[] buf = new char[2048];
                StringBuilder sb = new StringBuilder();
                int len;
                while ((len = reader.read(buf)) != -1) {
                    sb.append(buf, 0, len);
                }
                ret = sb.toString();
                reader.close();
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
