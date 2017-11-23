package com.gsonkeno.hot.utils;

import java.util.UUID;

/**
 * Created by gaosong on 2017-05-07.
 */
public class KeyTool {

    public  static  String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");

    }

    public static void main(String[] args) {
        System.out.println(getUUID().length());
    }
}
