package com.gsonkeno.utils;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ftp文件操作工具类
 * 使用模板如下:
 * <pre>{@code
 *     FtpHolder holder = new FtpHolder(...);
 *     try{
 *         holder.login();
 *         //doSomething
 *     }finally{
 *         holder.logout();
 *     }
 *
 * }
 * </pre>
 * Created by gaosong on 2017-09-20
 */
public class FtpHolder{
    private FTPClient ftpClient;
    private String userName;
    private String password;
    private String encoding;
    private String ip;
    private int port;


    public FtpHolder( String userName, String password, String encoding, String ip, int port) {
        this.userName = userName;
        this.password = password;
        this.encoding = encoding;
        this.ip = ip;
        this.port = port;
    }

    public boolean login(){
        ftpClient = new FTPClient();
        ftpClient.setControlEncoding(encoding);
        try {
            ftpClient.connect(ip,port);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        try {
            return ftpClient.login(userName, password);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取指定目录下所有文件的名称列表
     * @param path ftp服务器上的相对路径。若是根目录，可用<code> null </code>或者<code> / </code>
     * @return
     */
    public List<String> getFileNames(String path){
        List<String> fileNames = new ArrayList<>();
        try {
            FTPFile[] ftpFiles = ftpClient.listFiles(path,(file)->
                    !file.getName().equals(".") && !file.getName().equals(".."));
            for (FTPFile ftpFile : ftpFiles) {
                if (!ftpFile.isSymbolicLink()) fileNames.add(ftpFile.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            return fileNames;
        }
    }


    /**
     * 删除ftp目录文件，不可删除根路径
     * @param path ftp服务器相对路径，可以表示文件或文件夹
     * @return
     */
    public boolean deleteFile(String path){
        boolean b = false;
        int lastPos = path.lastIndexOf("/");
        String parentPath = path.substring(0,lastPos);
        String fileName = path.substring(lastPos + 1);
        try {
            FTPFile[] ftpFiles = ftpClient.listFiles(parentPath, (file)-> !file.getName().equals(".") && !file.getName().equals(".."));
            for(FTPFile ftpFile :ftpFiles){
                String name = ftpFile.getName();
                if (fileName.equals(name)){//遍历父目录，找到需要删除的文件或文件夹
                    if (ftpFile.isFile()){
                        return ftpClient.deleteFile(parentPath + "/" + name);
                    }else {
                        ftpFiles = ftpClient.listFiles(path, (file)-> !file.getName().equals(".") && !file.getName().equals(".."));
                        for (FTPFile file : ftpFiles){
                            deleteFile(path + "/"  + file.getName());
                        }
                        b = ftpClient.removeDirectory(path);
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return b;
    }

    /**
     * 删除ftp服务器根目录
     * @return
     */
    public boolean deleteRoot(){
        List<String> fileNames = getFileNames("/");

        for (String fileName : fileNames) {
            if (!deleteFile("/" + fileName))  return false;
        }
        return true;
    }
    /**
     * 退出登录
     * @return
     */
    public boolean logout() {
        try {
             return  ftpClient.logout();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
