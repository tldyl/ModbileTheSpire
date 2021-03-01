package com.megacrit.cardcrawl.android.mods.helpers;

public class ConvertHelper {
    public static Integer tryParseInt(String txt) {
        try {
            return Integer.parseInt(txt);
        } catch (NumberFormatException var2) {
            return null;
        }
    }

    public static Integer tryParseInt(String txt, Integer def) {
        try {
            return Integer.parseInt(txt);
        } catch (NumberFormatException var3) {
            return def;
        }
    }
}
