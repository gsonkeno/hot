package com.gsonkeno.utils;

import java.util.UUID;

/**
 * 主键生成器
 * Created by gaosong on 2017-05-07.
 */
public class KeyTool {

    public  static  String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");

    }

}
