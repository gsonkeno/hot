package com.gsonkeno.utils;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * Created by gaosong on 2017-08-27
 */
public class PackageUtilsTest {

    //测试获取jar包下的类
    @Test
    public void testGetClassesInJar() throws IOException {
        String packageName = "org.apache.commons.io.output";
        //PackageUtils.getClasses(packageName,true);

        packageName = "org.apache.commons.lang";
        // packageName = "com.gsonkeno.hot.elasticsearch";
        List<Class> classes = PackageUtils.getClasses(packageName, false);

        for (Class aClass : classes) {
            System.out.println(aClass);
        }
    }

    //测试获取文件目录中的类
    //该测试案例会获取到测试类路径和原类路径下的所有类
    @Test
    public void testGetClassesInDir() throws IOException {
        List<Class> classes = PackageUtils.getClasses("com.gsonkeno.utils", true);
        for (Class aClass : classes) {
            System.out.println(aClass);
        }
    }
}
