package com.gsonkeno.utils;

/**
 * Created by gaosong on 2017-04-12.
 * 位操作
 */
public class BitTest {

    public static void main(String[] args) {
        long i = -1L;
        //数字在计算机中都是以补码的形式存储的，正数的补码是自身，负数的补码是绝对值的反码加一
        //如-1的补码:0B 0000 0001(1的二进制原码)->0B 1111 1110(1的二进制反码)->0B 1111 1111(补码表示-1)
        // 0B 1111 1111左移两位 0B 1111 1100
        // 补码0B 1111 1100 ->反码 0B 0000 0011->原码0B 0000_0100 即十进制4
        // 因此-1 左移两位得-4
        System.out.println(i<<2);

        // -1左移六位得0B 1100 0000
        //0B 1111 1111 异或 0B 1100 0000得0B 0011 1111即
        System.out.println(-1 ^ -1<<6);

    }
}
