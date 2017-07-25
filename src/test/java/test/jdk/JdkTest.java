package test.jdk;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by gaosong on 2017-04-13.
 */
public class JdkTest {

    @Test
    public void test(){
        List<String> list = new ArrayList<String>();
        list.add("tm");
        list.add("2ms");
        String[] sArr = new String[]{};
        String[] strings = list.toArray(sArr);
        System.out.println(Arrays.toString(strings));
    }
}
