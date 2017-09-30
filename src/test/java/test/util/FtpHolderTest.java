package test.util;

import com.gsonkeno.hot.utils.FtpHolder;
import org.junit.Test;

import java.util.List;

/**
 * Created by gaosong on 2017-09-20
 */
public class FtpHolderTest {
    public static void main(String[] args) {
        FtpHolder ftpHolder = new FtpHolder("zhongwenqing", "proud4",
                "utf-8", "172.16.58.128", 21);
        boolean login = ftpHolder.login();
        System.out.println("登录结果:" + login);

        List<String> fileNames = ftpHolder.getFileNames(null);
        System.out.println("路径下的所有文件:" + fileNames);

        String path = "/goujian";
        fileNames = ftpHolder.getFileNames(path);
        System.out.println(path + "路径下的所有文件:" + fileNames);

        ftpHolder.deleteFile("/test.txt");
    }

    @Test
    public void deleteFile(){
        FtpHolder ftpHolder = new FtpHolder("zhongwenqing", "proud4",
                "utf-8", "172.16.58.128", 21);
        boolean login = ftpHolder.login();

        boolean b = ftpHolder.deleteFile("/test");
        System.out.println(b);
    }
}
