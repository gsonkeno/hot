package com.gsonkeno.hot.sql;

import com.microsoft.sqlserver.jdbc.SQLServerConnectionPoolDataSource;
import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import oracle.jdbc.pool.OracleConnectionPoolDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Created by gaosong on 2017-05-07.
 */
public class DbManager {
    private JdbcTemplate jdbc;

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

            case Orcal:
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

        jdbc = new JdbcTemplate(cpds);
    }

    public JdbcTemplate getJdbc(){
        return this.jdbc;
    }

}
