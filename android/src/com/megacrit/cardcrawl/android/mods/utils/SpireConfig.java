package com.megacrit.cardcrawl.android.mods.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class SpireConfig {
    private static final String EXTENSION = "properties";
    private Properties properties;
    private File file;

    public static String makeFilePath(String modName, String fileName) {
        return makeFilePath(modName, fileName, EXTENSION);
    }

    public static String makeFilePath(String modName, String fileName, String ext) {
        String dirPath;
        if (modName == null) {
            dirPath = ConfigUtils.CONFIG_DIR + File.separator;
        } else {
            dirPath = ConfigUtils.CONFIG_DIR + File.separator + modName + File.separator;
        }

        String filePath = dirPath + fileName + "." + ext;
        File dir = new File(dirPath);
        dir.mkdirs();
        return filePath;
    }

    public SpireConfig(String modName, String fileName) throws IOException {
        this(modName, fileName, new Properties());
    }

    public SpireConfig(String modName, String fileName, Properties defaultProperties) throws IOException {
        this.properties = new Properties(defaultProperties);
        String filePath = makeFilePath(modName, fileName);
        this.file = new File(filePath);
        this.file.createNewFile();
        this.load();
    }

    public void load() throws IOException {
        this.properties.load(new FileInputStream(this.file));
    }

    public void save() throws IOException {
        this.properties.store(new FileOutputStream(this.file), (String)null);
    }

    public boolean has(String key) {
        return this.properties.containsKey(key);
    }

    public void remove(String key) {
        this.properties.remove(key);
    }

    public void clear() {
        this.properties.clear();
    }

    public String getString(String key) {
        return this.properties.getProperty(key);
    }

    public void setString(String key, String value) {
        this.properties.setProperty(key, value);
    }

    public boolean getBool(String key) {
        return Boolean.parseBoolean(this.getString(key));
    }

    public void setBool(String key, boolean value) {
        this.setString(key, Boolean.toString(value));
    }

    public int getInt(String key) {
        return Integer.parseInt(this.getString(key));
    }

    public void setInt(String key, int value) {
        this.setString(key, Integer.toString(value));
    }

    public float getFloat(String key) {
        return Float.parseFloat(this.getString(key));
    }

    public void setFloat(String key, float value) {
        this.setString(key, Float.toString(value));
    }
}
