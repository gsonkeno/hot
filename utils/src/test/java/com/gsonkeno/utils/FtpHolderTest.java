package com.gsonkeno.utils;

import org.junit.Test;

import java.util.List;

/**
 * Created by gaosong on 2017-09-20
 */
public class FtpHolderTest {
    public static void main(String[] args) {

    }

    //登录查询指定目录文件列表
    @Test
    public void testQueryFileList(){
        FtpHolder ftpHolder = new FtpHolder("gaosong", "suntek",
                "gbk", "192.168.137.2", 21);

        try {
            boolean login = ftpHolder.login();
            System.out.println("登录成功?:" + login);

            String path = null;
            List<String> fileNames = ftpHolder.getFileNames(path);
            System.out.println("路径" + path + "下的所有文件:" + fileNames);

            path = "/";
            fileNames = ftpHolder.getFileNames(path);
            System.out.println("路径" + path + "下的所有文件:" + fileNames);

            path = "/gs";
            fileNames = ftpHolder.getFileNames(path);
            System.out.println("路径" + path + "下的所有文件:" + fileNames);

        }finally {
            System.out.println("退出成功?:" + ftpHolder.logout());
        }
    }

    //测试删除文件或目录(非根目录)
    @Test
    public void deleteFile(){
        FtpHolder ftpHolder = new FtpHolder("gaosong", "suntek",
                "gbk", "192.168.137.2", 21);
        try {
            boolean login = ftpHolder.login();
            System.out.println("登录成功?:" + login);

            String path = "/";
            boolean b = ftpHolder.deleteFile(path);
            System.out.println("删除" + path + "成功?:" + b);

        }finally {
            System.out.println("退出成功?:" + ftpHolder.logout());
        }
    }

    //测试删除ftp根目录下所有文件
    @Test
    public void deleteRoot(){
        FtpHolder ftpHolder = new FtpHolder("gaosong", "suntek",
                "gbk", "192.168.137.2", 21);
        try {
            boolean login = ftpHolder.login();
            System.out.println("登录成功?:" + login);
            boolean b = ftpHolder.deleteRoot();
            System.out.println("删除/成功?:" + b);

        }finally {
            System.out.println("退出成功?:" + ftpHolder.logout());
        }
    }
}
