package test.util;

import java.util.Arrays;
import java.util.List;

/**
 * Created by gaosong on 2017-04-05.
 */
public class CollectionTest {
    public static void main(String[] args) {
        List<String> listA = Arrays.asList("A","B","E", "D");
        List<String> listB = Arrays.asList("A","B","E","F");
        System.out.println(listB.containsAll(listA));
    }

}
