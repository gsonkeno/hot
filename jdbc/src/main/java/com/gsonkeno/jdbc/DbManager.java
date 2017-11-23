package com.gsonkeno.jdbc;

import com.microsoft.sqlserver.jdbc.SQLServerConnectionPoolDataSource;
import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import oracle.jdbc.pool.OracleConnectionPoolDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Created by gaosong on 2017-05-07.
 */
public class DbManager {
    private JdbcTemplateAdapter jdbc;

    /**
     *
     * @param url 常见的url类型有如下<br>
     *            <ul>
     *            <li>jdbc:sqlserver://172.16.56.231:1433;databaseName=videoweb_sw</li>
     *            <li>jdbc:mysql://localhost:3306/database?useUnicode=true&characterEncoding=utf8</li>
     *            <li>jdbc:oracle:thin:@172.16.58.40:1521:orcl</li>
     *            </ul>
     *
     * @param userName
     * @param passWord
     * @param dbType
     */
    public DbManager(String url, String userName, String passWord, DbType dbType){
        DataSource cpds = null;
        switch (dbType){
            case SQLSERVER:
                cpds = new SQLServerConnectionPoolDataSource();
                ((SQLServerConnectionPoolDataSource)cpds).setURL(url);
                ((SQLServerConnectionPoolDataSource)cpds).setUser(userName);
                ((SQLServerConnectionPoolDataSource)cpds).setPassword(passWord);
                break;

            case MYSQL:
                cpds = new MysqlConnectionPoolDataSource();
                ((MysqlConnectionPoolDataSource)cpds).setURL(url);
                ((MysqlConnectionPoolDataSource)cpds).setUser(userName);
                ((MysqlConnectionPoolDataSource)cpds).setPassword(passWord);
                break;

            case Orcl:
                try {
                    cpds = new OracleConnectionPoolDataSource();
                    ((OracleConnectionPoolDataSource)cpds).setURL(url);
                    ((OracleConnectionPoolDataSource)cpds).setUser(userName);
                    ((OracleConnectionPoolDataSource)cpds).setPassword(passWord);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;

        }

        jdbc = new com.gsonkeno.jdbc.JdbcTemplateAdapter(cpds);
    }

    public com.gsonkeno.jdbc.JdbcTemplateAdapter getJdbc(){
        return this.jdbc;
    }

}
