package com.gsonkeno.utils;

import org.junit.Test;

import java.util.Map;

/**
 * Created by gaosong on 2017-11-22
 */
public class ObjectUtilsTest {

    @Test
    public void testToString(){
        float f = 1.822f;
        System.out.println(ObjectUtils.toString(f));

        Map map = null;
        System.out.println(ObjectUtils.toString(map));
    }

    @Test
    public void testToInt(){
        float f = 2.10f;
        System.out.println(ObjectUtils.toInt(f));

        System.out.println(ObjectUtils.toInt("dad"));
    }

    @Test
    public void testRegex(){
        System.out.println("1314".matches("\\d+"));
        System.out.println("13.14".matches("\\d+[.]\\d++"));
    }
}
