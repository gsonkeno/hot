package com.gsonkeno.utils;

import org.junit.Test;

import java.io.IOException;

/**
 * Created by gaosong on 2017-11-24
 */
public class FileUtilsTest {

    @Test
    public void testCreateFile() throws IOException {
        //getResource()方法定位文件的位置时，文件必须存在，否则会报错
        System.out.println(this.getClass().getResource("/").getPath());
        FileUtils.forceCreateFile(this.getClass().getResource("/m.txt").getFile());
    }
}
