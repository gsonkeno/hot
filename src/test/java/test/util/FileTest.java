package test.util;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by gaosong on 2017-04-12.
 */
public class FileTest {


    public static void main(String[] args) throws IOException {
        
//        File file = new File("E:\\ideaProject\\summer\\src\\main\\resources\\mm\\nn");
//        FileInputStream is = new FileInputStream(file);
//        byte[] b = new byte[1024];
//        is.read(b);
//        FileUtils.touch(new File("E:\\ideaProject\\summer\\src\\main\\resources\\mm\\nn"));

    }

    @Test
    public void create(){
//        File file = new File("");
//        file.delete();
    }

    @Test
    public void createDir() throws IOException {
        FileUtils.deleteQuietly(new File("E:\\ideaProject\\summer\\src\\main\\resources\\mm\\nn"));
        FileUtils.forceMkdir(new File("E:\\ideaProject\\summer\\src\\main\\resources\\mm\\nn"));
    }

    @Test
    public  void delete(){
        File file  =new File("C:\\dad\\ca.txt");
        boolean delete = file.delete();
        System.out.println(delete); //文件不存在，删除失败，不报异常，返回false
    }


}
