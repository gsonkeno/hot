package com.gsonkeno.jdbc;

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
                "jdbc:mysql://127.0.0.1:3306/bs?serverTimezone=UTC",
                "root", "123456", DbType.MYSQL);

        JdbcTemplate jdbc = dbManager.getJdbc();


        String sql = "select * from GAME_RESULT";

        List<Map<String, Object>> list = jdbc.queryForList(sql);
        System.out.println(list);
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
