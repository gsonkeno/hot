package com.gsonkeno.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 学习Spring Jdbc中，原生的SqlException类无法表明造成异常的原因，原因需要由具体的jdbc厂商来提供
 * 如果你需要了解到具体的异常原因，你必须要捕获异常，并通过异常的属性如getMessage来拿到
 * 而且造成异常的原因往往是无法修复的，你可能仅仅是做下日志记录，什么其他的事情都做不了。
 *
 * Spring4实战中有说到，能够触发SQLException的问题通常在catch代码块中无法解决的
 * Created by gaosong on 2017-11-16
 */
public class JdbcTest {
    public static final String url = "jdbc:mysql://127.0.0.1/student";
    public static final String name = "com.mysql.jdbc.Driver";
    public static final String user = "root";
    public static final String password = "123456";

    public Connection conn = null;
    public PreparedStatement pst = null;

    public JdbcTest(String sql) {

        try {
            Class.forName(name);//指定连接类型
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        try {
            conn = DriverManager.getConnection(url, user, password);//获取连接
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getErrorCode());
            System.out.println(e.getSQLState());
            e.printStackTrace();
        }
        try {
            pst = conn.prepareStatement(sql);//准备执行语句
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }

    }

    public static void main(String[] args) {
        new JdbcTest("select * from GS");
    }
}
