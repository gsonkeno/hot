package test.util;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by gaosong on 2017-09-20
 */
public class FtpUtilsTest1 {
    static FTPClient ftp;
    public static void main(String[] args) throws IOException {
        ftp = new FTPClient();
        ftp.setControlEncoding("utf-8");

//        FTPClientConfig config = new FTPClientConfig();
//        config.setServerLanguageCode("zh");
//        ftp.configure(config);


        String ip = "172.16.58.128";
        int port = 21;
        String username = "zhongwenqing";
        String password = "proud4";
        ftp.connect(ip,port);

        System.out.print(Arrays.toString(ftp.getReplyStrings()));

        boolean login = ftp.login(username, password);
        System.out.println(Arrays.toString(ftp.getReplyStrings()));

        FTPFile[] ftpFiles = ftp.listFiles("/goujian/联网平台");

        for(FTPFile ftpFile : ftpFiles){
            System.out.println(ftpFile.getName());
        }


    }
}
