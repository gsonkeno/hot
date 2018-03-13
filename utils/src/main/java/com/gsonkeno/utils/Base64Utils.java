package com.gsonkeno.utils;
import sun.misc.BASE64Encoder;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by gaosong on 2018-02-26
 */
public class Base64Utils {

    /**
     * 获取本地文件的base64编码
     * @param imgFile
     * @return
     * @throws IOException
     */
    public static String getBase64Str(String imgFile) throws IOException {
        FileInputStream fis = new FileInputStream(imgFile);
        byte[] data = new byte[fis.available()];
        fis.read(data);
        BASE64Encoder base64Encoder = new BASE64Encoder();
        String encode = base64Encoder.encode(data);
        return encode;
    }


}
