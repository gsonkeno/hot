package com.gsonkeno.utils;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import java.io.*;

/**
 * Created by gaosong on 2018-01-11
 */
public class CompressZipApache {
    static final int BUFFER = 8192;


    private void compress(File file, ZipOutputStream out, String basedir) {
        /* 判断是目录还是文件 */
        if (file.isDirectory()) {
            this.compressDirectory(file, out, basedir);
        } else {
            this.compressFile(file, out, basedir);
        }
    }

    /** 压缩一个目录 */
    private void compressDirectory(File dir, ZipOutputStream out, String basedir) {
        if (!dir.exists())
            return;

        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            /* 递归 */
            compress(files[i], out, basedir + dir.getName() + "/");
        }
    }

    /** 压缩一个文件 */
    private void compressFile(File file, ZipOutputStream out, String basedir) {
        if (!file.exists()) {
            return;
        }
        try {
            BufferedInputStream bis = new BufferedInputStream(
                    new FileInputStream(file));
            ZipEntry entry = new ZipEntry(basedir + file.getName());
            out.putNextEntry(entry);
            int count;
            byte data[] = new byte[BUFFER];
            while ((count = bis.read(data, 0, BUFFER)) != -1) {
                out.write(data, 0, count);
            }
            bis.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void compress(String srcPathName, String zipFile) {
        File file = new File(srcPathName);
        if (!file.exists())
            throw new RuntimeException(srcPathName + "不存在！");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
//            CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream,
//                    new CRC32());  //不加CRC32，一样可以生成文件。关于数据如何校验，请高手指点
            ZipOutputStream out = new ZipOutputStream(fileOutputStream);
            out.setEncoding("gbk");  //如果不加此句，压缩文件依然可以生成，只是在打开和解压的时候，会显示乱码，但是还是会解压出来
            String basedir = "";
            compress(file, out, basedir);
            out.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        CompressZipApache zc = new CompressZipApache();
        long begin = System.currentTimeMillis();
        zc.compress("D:\\工作\\公司人脸图片","D:\\test.zip");  //压缩一个文件夹
        long end = System.currentTimeMillis();

        System.out.println("耗时(s):" + (end -begin)/1000);
    }
}
