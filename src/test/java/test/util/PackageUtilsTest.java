package test.util;

import com.gsonkeno.hot.utils.PackageUtils;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by gaosong on 2017-08-27
 */
public class PackageUtilsTest {

    @Test
    public void testgetClassesInJar() throws IOException {
        String packageName = "org.apache.commons.io.output";
        //PackageUtils.getClasses(packageName,true);

        packageName = "org.apache.commons.lang";
        packageName = "com.gsonkeno.hot.elasticsearch";
        PackageUtils.getClasses(packageName,false);

    }

    @Test
    public void getClassInJar() throws IOException {
        URL resource = Thread.currentThread().getContextClassLoader().getResource("");
        System.out.println(resource.getPath());
        JarFile jarFile = new JarFile("E:\\ideaProjects2017\\hot\\test\\target\\test-1.0-SNAPSHOT.jar");
        Enumeration<JarEntry> entrys = jarFile.entries();

        while (entrys.hasMoreElements()) {
            JarEntry jarEntry = entrys.nextElement();
            String entryName = jarEntry.getName();

            System.out.println(entryName);

            if (entryName.endsWith(".class")){
                try {
                    Class<?> subClass = Class.forName(entryName.replace(".class", "").replace("/", "."));

                    System.out.println(subClass + "is subClass of Thread" + subClass.isAssignableFrom(Thread.class));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (NoClassDefFoundError error){
                    error.printStackTrace();
                }
            }

//            if ((entryName.matches(String.format("^(%s)(.*)(\\.class)$",packagePath)) && recursive)
//                    ||(entryName.matches(String.format("^(%s)/[^/]+(\\.class)$",packagePath)) &&!recursive)){
//
//                //比如 org/apache/commons/io/output/TaggedOutputStream.class
//                entryName = entryName.replace("/", ".")
//                        .replace(".class","");
//                classList.add(Class.forName(entryName));
//            }
        }

    }

}
