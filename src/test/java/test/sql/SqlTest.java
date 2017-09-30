package test.sql;

import com.gsonkeno.hot.sql.DbManager;
import com.gsonkeno.hot.sql.DbType;
import com.gsonkeno.hot.sql.JdbcTemplateAdapter;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by gaosong on 2017-06-26.
 */
public class SqlTest {

    public static void main(String[] args) {
        DbManager dbManager = new DbManager(
                "jdbc:sqlserver://172.16.56.231:1433;databaseName=videoweb_sw",
                "videoweb", "suntek", DbType.SQLSERVER);

        JdbcTemplate jdbc = dbManager.getJdbc();


        String sql = "update CA_SEARCH_TASK set PROGRESS = " +
                "cast( (CALLBACK_COUNT + ?) as float)/SEND_COUNT," +
                " CALLBACK_COUNT = (CALLBACK_COUNT + ?) where TASK_ID = ?";

//        String sql = "update CA_SEARCH_TASK set PROGRESS = ? where TASK_ID = ?";

        int value = jdbc.update(sql, 2,2,  "d5839cd21c1e45f48001160f6002b2d5");
        System.out.println(value);
    }

    @Test
    public void testInsertOrcl() throws IOException {
        DbManager dbManager = new DbManager("jdbc:oracle:thin:@172.16.58.40:1521:orcl","oprdata","suntek",DbType.Orcl);
        JdbcTemplateAdapter jdbc = dbManager.getJdbc();
        List<Map<String, Object>> list = jdbc.queryForList("select * from GAO_SONG");
        System.out.println(list);

//        int i = jdbc.executeBlob("insert into GAO_SONG (NAME, IDENTITYID,LIBNAME,PIC) values (?,?,?,?)"
//                , new Object[]{"苏伟", "4231276831", "排查库", FileUtils.localFileToByte(System.getProperty("user.dir") +
//                        "/src/test/resources/1.jpg")});
//        System.out.println(i);
    }
}
