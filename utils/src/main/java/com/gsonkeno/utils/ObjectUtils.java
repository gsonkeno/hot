package com.gsonkeno.utils;

/**
 * Created by gaosong on 2017-04-07.
 */
public class ObjectUtils {

    public static  String toString(Object obj){
        return obj == null? "":obj.toString();
    }

    /**
     * 对象转数字，浮点型小数舍去小数, 无法强转时将抛出 NumberFormatException
     * @param obj
     * @return
     */
    public static int toInt(Object obj){
        String s = toString(obj);
        if (s.matches("\\d[.]\\d")){
            s = s.split("\\.")[0];
        }
        return Integer.parseInt(s);
    }

}
