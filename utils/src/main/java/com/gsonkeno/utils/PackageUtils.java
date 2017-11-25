package com.gsonkeno.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 包工具类
 * Created by gaosong on 2017-08-27
 */
public class PackageUtils {

    private enum Protocol{
        JAR("jar"), FILE("file");

        private String type;
        Protocol(String type){
            this.type = type;
        }
    }

    /**
     * 获取包下的类(类可能位于jar包中，也可能位于文件目录中)
     * @param packageName 包名,如 org.apache.commons.lang
     * @param recursive 是否递归
     * @throws IOException
     */
    public static List<Class> getClasses(String packageName, boolean recursive) throws IOException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace(".","/");
        Enumeration<URL> resources = loader.getResources(path);

        List<Class> classes = new ArrayList<Class>();
        while (resources.hasMoreElements()){
            URL resource = resources.nextElement();
            String protocol = resource.getProtocol();

            if (protocol.equals(Protocol.JAR.type)){
                classes.addAll(getClassesInJar(resource.getPath(),recursive));
            }else if (protocol.equals(Protocol.FILE.type)){
               classes.addAll(getClassesInDir(new File(resource.getPath()),packageName,recursive));
            }

        }
        return classes;
    }


    /**
     * 获取jar包中指定路径下的类
     * @param resPath  指定路径,如
     * file:/E:/maven-repository/commons-io/commons-io/2.5/commons-io-2.5.jar!/org/apache/commons/io/output
     * @param recursive 是否递归获取
     */
    private static List<Class> getClassesInJar(String resPath, boolean recursive){
        String[] paths = resPath.split("!");
        String jarPath = paths[0].substring(paths[0].indexOf("/"));
        String packagePath = paths[1].substring(1);

        List<Class> classList = new ArrayList<>();
        try {
            JarFile jarFile = new JarFile(jarPath);
            Enumeration<JarEntry> entrys = jarFile.entries();
            while (entrys.hasMoreElements()) {
                JarEntry jarEntry = entrys.nextElement();
                String entryName = jarEntry.getName();

                if ((entryName.matches(String.format("^(%s)(.*)(\\.class)$",packagePath)) && recursive)
                    ||(entryName.matches(String.format("^(%s)/[^/]+(\\.class)$",packagePath)) &&!recursive)){

                    //比如 org/apache/commons/io/output/TaggedOutputStream.class
                    entryName = entryName.replace("/", ".")
                            .replace(".class","");
                    classList.add(Class.forName(entryName));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classList;
    }

    /**
     * 获取文件目录中指定路径下的类
     * @param dir  指定目录
     * @param packageName 包名
     * @param recursive 是否递归获取
     */
    private static List<Class> getClassesInDir(File dir,String packageName, boolean recursive){
        List<Class> classes = new ArrayList<>();

        if (!dir.exists()) {
            return classes;
        }
        File[] files = dir.listFiles();
        for (File file : files) {

            if  (file.getName().endsWith(".class")) {
                try {
                    classes.add(Class.forName(packageName + '.' + file.getName().replace(".class","")));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

            if (recursive && file.isDirectory()) {
                    classes.addAll(getClassesInDir(file, packageName + "." + file.getName(),recursive));
            }

        }

        return classes;
    }
}
