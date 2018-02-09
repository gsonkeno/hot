package com.gsonkeno.utils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Created by gaosong on 2017-03-31.
 * 解压缩工具类
 */
public class ZipUtils {

      private static int BUFFER_SIZE = 8196;
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
            byte[] b = new byte[BUFFER_SIZE];
            int len;
            while ((len = is.read(b)) > 0) {
                zos.write(b, 0, len);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 压缩sourceDir目录下的所有文件到zipDir目录下名称为zipName的zip文件中
     * @param sourceDir
     * @param zipName
     * @param zipDir
     */
    public static void compressDir(String sourceDir, String zipName, String zipDir){
        File sourceFile = new File(sourceDir);
        FileInputStream fis;
        BufferedInputStream bis = null;
        FileOutputStream fos;
        ZipOutputStream zos = null;

        if(sourceFile.exists() == false){
            System.out.println("待压缩的文件目录："+sourceDir+"不存在.");
        }else{
            try {
                File zipFile = new File(zipDir +File.separator + zipName );
                if(zipFile.exists()){
                    System.out.println(zipDir + "目录下存在名字为:" + zipName +"打包文件.");
                }else{
                    File[] sourceFiles = sourceFile.listFiles();
                    if(null == sourceFiles || sourceFiles.length<1){
                        System.out.println("待压缩的文件目录：" + sourceDir + "里面不存在文件，无需压缩.");
                    }else{
                        fos = new FileOutputStream(zipFile);
                        zos = new ZipOutputStream(new BufferedOutputStream(fos));
                        byte[] bufs = new byte[BUFFER_SIZE];

                        for(int i=0;i<sourceFiles.length;i++){
                            compress("",sourceFiles[i],zos);
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally{
                //关闭流
                try {
                    if(null != bis) bis.close();
                    if(null != zos) zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static  void compress(String prefix, File sourceFile, ZipOutputStream zos) throws IOException {
        if (sourceFile.isDirectory()){
            File[] files = sourceFile.listFiles();
            for (File file : files){
                compress(sourceFile.getName(),file, zos);
            }
        }else{
            //创建ZIP实体，并添加进压缩包
            ZipEntry zipEntry = new ZipEntry(prefix + File.separator + sourceFile.getName());
            zos.putNextEntry(zipEntry);
            //读取待压缩的文件并写进压缩包里
            FileInputStream fis = new FileInputStream(sourceFile);
            BufferedInputStream bis = new BufferedInputStream(fis, BUFFER_SIZE);
            int read = 0;
            byte[] bufs = new byte[BUFFER_SIZE];
            while((read=bis.read(bufs, 0, BUFFER_SIZE)) != -1){
                zos.write(bufs,0,read);
            }
        }
    }


    /**
     * 解压zip文件中指定文件 <code> entryName</code> 到指定路径<code>outFilePath</code>
     *
     * @param zipFilePath zip文件
     * @param outFilePath 解压后文件路径. 该文件可不存在，但其父目录必须存在
     * @param entryName   指定文件名称,相对zip文件的根路径，如zip文件里的a/b/c.txt
     */
    public static void deCompress(String zipFilePath, String outFilePath, String entryName) {

        try(ZipFile zipFile = new ZipFile(zipFilePath);
            InputStream is = zipFile.getInputStream(zipFile.getEntry(entryName));
            FileOutputStream fos = new FileOutputStream(new File(outFilePath))) {

            byte[] b = new byte[BUFFER_SIZE];
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

                    byte[] b = new byte[BUFFER_SIZE];
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



}
