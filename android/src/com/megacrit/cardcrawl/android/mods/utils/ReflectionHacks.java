package com.megacrit.cardcrawl.android.mods.utils;

import com.megacrit.cardcrawl.android.SpireAndroidLogger;
import com.megacrit.cardcrawl.android.mods.Pair;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class ReflectionHacks {
    private static final Map<Pair<Class<?>, String>, Field> fieldMap = new HashMap<>();
    private static final SpireAndroidLogger logger = SpireAndroidLogger.getLogger(ReflectionHacks.class);
    public static <T> T getPrivateStatic(Class<?> objClass, String fieldName) {
        try {
            return (T) getCachedField(objClass, fieldName).get(null);
        } catch (Exception var3) {
            logger.error("Exception occurred when getting private static field " + fieldName + " of " + objClass.getName(), var3);
            return null;
        }
    }

    public static <T> T getPrivate(Object obj, Class<?> objClass, String fieldName) {
        try {
            return (T) getCachedField(objClass, fieldName).get(obj);
        } catch (Exception var4) {
            logger.error("Exception occurred when getting private field " + fieldName + " of " + objClass.getName(), var4);
            return null;
        }
    }

    public static Field getCachedField(Class<?> clz, String fieldName) {
        Pair<Class<?>, String> pair = new Pair<>(clz, fieldName);
        Field ret = fieldMap.getOrDefault(pair, null);
        if (ret == null) {
            try {
                ret = clz.getDeclaredField(fieldName);
                ret.setAccessible(true);
                fieldMap.put(pair, ret);
            } catch (NoSuchFieldException var5) {
                logger.error("Exception occurred when getting field " + fieldName + " of " + clz.getName(), var5);
                var5.printStackTrace();
            }
        }

        return ret;
    }
}
