package com.gsonkeno.utils;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by gaosong on 2017-04-12.
 */
public class FileUtils {

    /**
     * 强制创建新的文件，不管其是否已经存在
     * @param filePath 文件路径
     * @throws IOException
     */
    public static File forceCreateFile(String filePath) throws IOException {
        File file = new File(filePath);
        org.apache.commons.io.FileUtils.deleteQuietly(file);
        org.apache.commons.io.FileUtils.touch(file);
        return new File(filePath);
    }

    /**
     * 强制创建新的文件夹，不管其是否已经存在
     * @param dirPath 文件夹路径
     * @throws IOException
     */
    public static void forceCreateDir(String dirPath) throws IOException {
        File file = new File(dirPath);
        org.apache.commons.io.FileUtils.deleteQuietly(file);
        org.apache.commons.io.FileUtils.forceMkdir(file);
    }

    /**
     * 本地文件转字节数组
     * @param filePath
     * @return
     * @throws IOException
     */
    public static byte[] localFileToByte(String filePath) throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        return IOUtils.toByteArray(fis);
    }
}
