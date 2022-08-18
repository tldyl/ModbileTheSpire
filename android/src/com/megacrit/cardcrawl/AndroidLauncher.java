package com.megacrit.cardcrawl;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.surfaceview.FillResolutionStrategy;
import com.megacrit.cardcrawl.android.mods.Loader;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import dalvik.system.DexClassLoader;

import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

public class AndroidLauncher extends AndroidApplication {
    public static AssetManager assetManager;
    public static AndroidLauncher instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instance = this;
        readFromAssets("hack_dex.jar");
        assetManager = getAssets();
        Loader.loadMods(Environment.getExternalStorageDirectory().getAbsolutePath().concat("/SpireMods"));

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useWakelock = true;
        config.resolutionStrategy = new FillResolutionStrategy();
        initialize(new CardCrawlGame(Environment.getExternalStorageDirectory().getAbsolutePath().concat("/.prefs/")), config);
    }

    /**
     * 将位于assets目录的jar包中的类加载进虚拟机
     * @param name jar包路径+名称
     */
    private void readFromAssets(String name) {
        try {
            InputStream is = this.getResources().getAssets().open(name);
            String hackPath = Environment.getExternalStorageDirectory().getAbsolutePath().concat("/hack_dex.jar");
            System.out.println(hackPath);
            File file = new File(hackPath);
            if (!file.exists()) {
                file.createNewFile();
            }
            OutputStream os = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = is.read(buf)) != -1) {
                os.write(buf, 0, len);
            }
            os.flush();
            is.close();
            os.close();

            inject(hackPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 要注入的dex的路径
     *
     * @param path 路径
     */
    public void inject(String path) {
        try {
            // 获取classes的dexElements
            Class<?> cl = Class.forName("dalvik.system.BaseDexClassLoader");
            Object pathList = getField(cl, "pathList", getClassLoader());
            Object baseElements = getField(pathList.getClass(), "dexElements", pathList);

            // 获取patch_dex的dexElements（需要先加载dex）
            String dexopt = getDir("dexopt", 0).getAbsolutePath();
            DexClassLoader dexClassLoader = new DexClassLoader(path, dexopt, dexopt, getClassLoader());
            Object obj = getField(cl, "pathList", dexClassLoader);
            Object dexElements = getField(obj.getClass(), "dexElements", obj);

            // 合并两个Elements
            Object combineElements = combineArray(dexElements, baseElements);

            // 将合并后的Element数组重新赋值给app的classLoader
            setField(pathList.getClass(), "dexElements", pathList, combineElements);

            //======== 以下是测试是否成功注入 =================
            Object object = getField(pathList.getClass(), "dexElements", pathList);
            int length = Array.getLength(object);
            Log.e("CardCrawlGame", "length = " + length);

        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过反射获取对象的属性值
     */
    private Object getField(Class<?> cl, String fieldName, Object object) throws NoSuchFieldException, IllegalAccessException {
        Field field = cl.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(object);
    }

    /**
     * 通过反射设置对象的属性值
     */
    private void setField(Class<?> cl, String fieldName, Object object, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = cl.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }

    /**
     * 通过反射合并两个数组
     */
    private Object combineArray(Object firstArr, Object secondArr) {
        int firstLength = Array.getLength(firstArr);
        int secondLength = Array.getLength(secondArr);
        int length = firstLength + secondLength;

        Class<?> componentType = firstArr.getClass().getComponentType();
        Object newArr = Array.newInstance(componentType, length);
        for (int i = 0; i < length; i++) {
            if (i < firstLength) {
                Array.set(newArr, i, Array.get(firstArr, i));
            } else {
                Array.set(newArr, i, Array.get(secondArr, i - firstLength));
            }
        }
        return newArr;
    }
}
