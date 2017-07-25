package test.util;


import java.util.logging.Logger;

/**
 * Created by gaosong on 2017-04-12.
 * 位操作
 */
public class BitTest {

    public static void main(String[] args) {
        long i = -1L;
        // 0xFFFF_FFFF左移两位 FFFF_FF00
        // 补码FFFF_FF00 ->反码 FFFF_FEFF->原码0000_0100 即十进制4
        // 因此-1 左移两位得-4
        System.out.println(i<<2);

        // -1即 0xFFFF_FFFF 左移六位即 FFFF_FF_1100_0000
        //FFFF_FF_1100_0000 异或0xFFFF_FF1111_1111得0x0000_00_00111111
        System.out.println(-1 ^ -1<<6);

    }
}
