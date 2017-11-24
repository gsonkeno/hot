package com.gsonkeno.utils;

import org.junit.Test;

import java.io.File;

/**
 * 路径主要使用两种
 * 1)工程路径 工程路径基准为工程目录(project或module)
 * 2)类路径  class.getResource("/")类路径的根路径
 * Created by gaosong on 2017-11-23
 */
public class ZipUtilsTest {

    //测试解压文件
    @Test
    public void testDeCompress(){
        //获取工作目录
        System.out.println(System.getProperty("user.dir"));

        //测试工程路径下文件是否存在
        String zipFilePath = "src/test/resources/test.zip";
        File file = new File(zipFilePath);
        System.out.println(file.exists());

        //测试类路径下文件是否存在
        File file1 = new File(this.getClass().getResource("/test.zip").getFile());
        System.out.println(file1.exists());

        //将文件解压到类路径下
//        ZipUtils.deCompress(this.getClass().getResource("/test.zip").getFile(),
//                this.getClass().getResource("/").getFile());

        ZipUtils.deCompress(this.getClass().getResource("/test.zip").getFile(),
                this.getClass().getResource("/").getFile() + "m.txt","b/c.txt");

    }
}
