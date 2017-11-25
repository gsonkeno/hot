package com.gsonkeno.utils;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ftp文件操作工具类
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
            boolean login = ftpClient.login(userName, password);
            return login;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * get all file's name at the specified path
     * @param path
     * @return
     */
    public List<String> getFileNames(String path){
        List<String> fileNames = new ArrayList<>();
        try {
            FTPFile[] ftpFiles = ftpClient.listFiles(path,(file)->{
                    return !file.getName().equals(".") && !file.getName().equals("..");
                });
            for (FTPFile ftpFile : ftpFiles) {
                if (!ftpFile.isSymbolicLink()) fileNames.add(ftpFile.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            return fileNames;
        }
    }


    public boolean deleteFile(String path){
        boolean b = false;
        int lastPos = path.lastIndexOf("/");
        String parentPath = path.substring(0,lastPos);
        String fileName = path.substring(lastPos + 1);

        try {
            FTPFile[] ftpFiles = ftpClient.listFiles(parentPath, (file)->{
                return !file.getName().equals(".") && !file.getName().equals("..");
            });
            for(FTPFile ftpFile :ftpFiles){
                String name = ftpFile.getName();
                if (fileName.equals(name)){
                    if (ftpFile.isFile()){
                        b =  ftpClient.deleteFile(parentPath + "/" + name);
                        if (b == false){
                            System.out.println("删除" +parentPath + "/" + name + "失败" );
                        }else {
                            System.out.println("删除" +parentPath + "/" + name + "成功" );
                        }
                        return b;
                    }else {
                        ftpFiles = ftpClient.listFiles(path, (file)->{
                            return !file.getName().equals(".") && !file.getName().equals("..");
                        });

                        for (FTPFile file : ftpFiles){
                            deleteFile(path + "/"  + file.getName());
                        }

                        b = ftpClient.removeDirectory(path);
                        if (b == false) System.out.println("删除目录" + path + "失败");
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return b;
    }

//    private boolean deleteDirectory(String path){
//
//        return true;
//    }
}
