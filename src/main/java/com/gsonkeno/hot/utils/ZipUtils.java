package com.gsonkeno.hot.utils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Created by gaosong on 2017-03-31.
 */
public class ZipUtils {

    /**
     * 压缩单个文件
     *
     * @param filePath    待压缩的文件路径
     * @param zipFilePath 压缩后的zip文件路径
     */
    public static void compress(String filePath, String zipFilePath) {
        try( ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFilePath));
             InputStream is = new FileInputStream(filePath)) {
            File file = new File(filePath);
            String fileName = file.getName();


            ZipEntry entry = new ZipEntry(fileName);

            zos.putNextEntry(entry);
            byte[] b = new byte[1024];
            int len;
            while ((len = is.read(b)) > 0) {
                zos.write(b, 0, len);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 解压zip文件中指定文件 <code> entryName</code> 到指定路径<code>outFilePath</code>
     *
     * @param zipFilePath zip文件
     * @param outFilePath 解压后文件路径
     * @param entryName   指定文件名称
     */
    public static void deCompress(String zipFilePath, String outFilePath, String entryName) {
        try(ZipFile zipFile = new ZipFile(zipFilePath);
            InputStream is = zipFile.getInputStream(zipFile.getEntry(entryName));
            FileOutputStream fos = new FileOutputStream(new File(outFilePath))) {

            byte[] b = new byte[1024];
            int len;
            while ((len = is.read(b)) > 0) {
                fos.write(b, 0, len);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 解压使用 <code>charset</code> 编码的压缩文件 <code>zipFilePath</code> 到指定目录 <code>toFileDir</code>
     *
     * @param zipFilePath 压缩文件
     * @param toFileDir   指定目录
     * @param charset     压缩文件编码，一般为 <code>Charset.forName("GBK")</code>
     */
    public static void deCompress(String zipFilePath, String toFileDir, Charset charset) {
        try(ZipFile zipFile = new ZipFile(zipFilePath, charset);) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();

            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();

                String outPath = toFileDir + File.separator + entry.getName();
                File outPathFile = new File(outPath);


                if (!outPathFile.getParentFile().exists()) {
                    outPathFile.getParentFile().mkdir();
                }

                if (entry.isDirectory()) {
                    outPathFile.mkdirs();
                    continue;
                }
                try ( InputStream is = zipFile.getInputStream(entry);
                      OutputStream os = new FileOutputStream(outPathFile)) {

                    byte[] b = new byte[1024];
                    int len;
                    while ((len = is.read(b)) > 0) {
                        os.write(b, 0, len);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 解压压缩文件 <code>zipFilePath</code> 至指定目录 <code>toFileDir</code>,<br>
     * 实际调用 {@link #deCompress(String, String, Charset)}
     *
     * @param zipFilePath 压缩文件
     * @param toFileDir   指定目录
     */
    public static void deCompress(String zipFilePath, String toFileDir) {
        deCompress(zipFilePath, toFileDir, Charset.forName("GBK"));
    }


    public static void main(String[] args) {
        compress("C:\\Users\\gaosong\\Desktop\\FollowPersonService.java",
                "C:\\Users\\gaosong\\Desktop\\test.zip");
//        deCompress("C:\\Users\\gaosong\\Desktop\\test.zip","C:\\Users\\gaosong\\Desktop\\FollowPersonService.java","FollowPersonService.java");
//        deCompress("C:\\Users\\gaosong\\Desktop\\XinjiangWatch.zip",
//                "C:\\Users\\gaosong\\Desktop\\folder");
    }
}
