package com.gsonkeno.utils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by gaosong on 2018-01-12
 */
public class Zip4JTest {

    public static void main(String[] args) throws ZipException {
        long begin = System.currentTimeMillis();

        File fileDir = new File("D:\\工作\\公司人脸图片");

        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);           // 压缩方式
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);    // 压缩级别

        ZipFile zipFile = new ZipFile("D:\\test2.zip");
        File [] subFiles = fileDir.listFiles();
        ArrayList<File> temp = new ArrayList<File>();
        Collections.addAll(temp, subFiles);
        zipFile.addFiles(temp, parameters);

        long end = System.currentTimeMillis();

        System.out.println("耗时(s):" + (end-begin)/1000);
    }
}
