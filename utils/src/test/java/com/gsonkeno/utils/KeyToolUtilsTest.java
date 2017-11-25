package com.gsonkeno.utils;

import org.junit.Test;

/**
 * Created by gaosong on 2017-11-25
 */
public class KeyToolUtilsTest {

    //测试获取UUID
    @Test
    public void testGetKey(){
        for (int i = 0; i < 100; i++) {
            System.out.println(KeyTool.getUUID());
        }
    }
}
