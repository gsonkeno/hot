package test.util;

import com.gsonkeno.hot.utils.PackageUtils;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by gaosong on 2017-08-27
 */
public class PackageUtilsTest {

    @Test
    public void testgetClassesInJar() throws IOException {
        String packageName = "org.apache.commons.io.output";
        //PackageUtils.getClasses(packageName,true);

        packageName = "org.apache.commons.lang";
        packageName = "com.gsonkeno.hot.elasticsearch";
        PackageUtils.getClasses(packageName,false);

    }

}
