package com.megacrit.cardcrawl.android.mods.utils;

import android.os.Environment;

import java.io.File;

public class ConfigUtils {
    private static final String APP_NAME = "ModTheSpire";
    public static final String CONFIG_DIR;

    public ConfigUtils() {
    }

    static {
        String basedir = Environment.getExternalStorageDirectory().getAbsolutePath();
        CONFIG_DIR = basedir + File.separator + APP_NAME;
        File directory = new File(CONFIG_DIR);
        if (!directory.exists()) directory.mkdirs();
    }
}
