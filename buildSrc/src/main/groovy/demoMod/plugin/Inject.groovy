package demoMod.plugin

import javassist.CannotCompileException
import javassist.ClassPool
import javassist.CtClass
import javassist.CtConstructor
import org.apache.commons.io.FileUtils

public class Inject {

    private static ClassPool pool= ClassPool.getDefault()

    /**
     * 添加classPath到ClassPool
     * @param libPath
     */
    public static void appendClassPath(String libPath) {
        pool.appendClassPath(libPath)
    }

    /**
     * 遍历该目录下的所有class，对所有class进行代码注入。
     * 其中以下class是不需要注入代码的：
     * --- 1. R文件相关
     * --- 2. 配置文件相关（BuildConfig）
     * --- 3. Application
     * @param path 目录的路径
     */
    public static void injectDir(String path) {
        pool.appendClassPath(path)
        File dir = new File(path)
        if (dir.isDirectory()) {
            dir.eachFileRecurse { File file ->

                String filePath = file.absolutePath
                if (filePath.endsWith(".class")
                        && !filePath.contains('R$')
                        && !filePath.contains('R.class')
                        && !filePath.contains("BuildConfig.class")
                        // 这里是application的名字，可以通过解析清单文件获得，先写死了
                        && !filePath.contains("AndroidLauncher.class")) {
                    // 这里是应用包名，也能从清单文件中获取，先写死
                    int index = filePath.indexOf("com\\megacrit\\cardcrawl")
                    if (index != -1) {
                        int end = filePath.length() - 6 // .class = 6
                        String className = filePath.substring(index, end).replace('\\', '.').replace('/', '.')
                        injectClass(className, path)
                    }
                }
            }
        }
    }

    /**
     * 这里需要将jar包先解压，注入代码后再重新生成jar包
     * @path jar包的绝对路径
     */
    public static void injectJar(String path) {
        if (path.endsWith("desktop-1.0.jar")) {
            File jarFile = new File(path)

            // jar包解压后的保存路径
            String jarZipDir = jarFile.getParent() + "/" + jarFile.getName().replace('.jar', '')

            File bakFile = new File(path + ".bak")
            println "bak file path:" + bakFile.getAbsolutePath()

            // 解压jar包, 返回jar包中所有class的完整类名的集合（带.class后缀）
            List classNameList = JarZipUtil.unzipJar(path, jarZipDir)

            // 删除原来的jar包
            FileUtils.copyFile(jarFile, bakFile)
            jarFile.delete()

            // 注入代码
            try {
                pool.appendClassPath(jarZipDir)
                for (String className : classNameList) {
                    if (className.endsWith(".class")
                            && !className.contains('R$')
                            && !className.contains('R.class')
                            && !className.contains("BuildConfig.class")) {
                        className = className.substring(0, className.length() - 6)
                        injectClass(className, jarZipDir)
                    }
                }
            } catch (Exception e) {
                e.printStackTrace()
            } finally {
                // 从新打包jar
                JarZipUtil.zipJar(jarZipDir, path)

                // 删除目录
                FileUtils.deleteDirectory(new File(jarZipDir))
            }
        }
    }

    private static void injectClass(String className, String path) {
        CtClass c = pool.getCtClass(className)
        if (c.isFrozen()) {
            c.defrost()
        }
        System.out.println("Injecting " + c.getName() + " ...")
        if (!c.getName().contains("com.megacrit")) {
            println "Skipped."
            return
        }
        if (!c.isInterface()) {
            CtConstructor[] cts = c.getDeclaredConstructors()
            if (cts == null || cts.length == 0) {
                insertNewConstructor(c)
            } else {
                try {
                    cts[0].insertBeforeBody("demoMod.hack.AntilazyLoad.call();")
                } catch (CannotCompileException ignored) {
                    println "Patch failed. This class will not be able to replace in android environment."
                }
            }
            c.writeFile(path)
            c.detach()
            println "Done."
        } else {
            System.out.println(c.getName() + " is an interface, skipped.")
        }
    }

    private static void insertNewConstructor(CtClass c) {
        try {
            CtConstructor constructor = new CtConstructor(new CtClass[0], c)
            constructor.insertBeforeBody("demoMod.hack.AntilazyLoad.call();")
            c.addConstructor(constructor)
        } catch (Exception ignored) {

        }
    }

}