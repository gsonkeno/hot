package test.sql;

import com.gsonkeno.hot.sql.DbManager;
import com.gsonkeno.hot.sql.DbType;
import org.springframework.jdbc.core.JdbcTemplate;

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
}
