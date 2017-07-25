package com.gsonkeno.hot.utils;

import org.apache.commons.lang.math.RandomUtils;

import java.util.UUID;

/**
 * Created by gaosong on 2017-05-07.
 */
public class KeyTool {

    public  static  String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");

    }
}
