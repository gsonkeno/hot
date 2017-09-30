package com.gsonkeno.hot.sql;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.jdbc.support.lob.LobHandler;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by gaosong on 2017-09-29
 * JdbcTemplate适配器
 */
public class JdbcTemplateAdapter extends JdbcTemplate {

    /**针对orcl而设的插入含二进制数据的行**/
    public  int executeBlob(String sql, Object[] plainPreparedStatement, byte[]... bytesPreparedStatement ){
        LobHandler lobHandler = new DefaultLobHandler();
        AbstractLobCreatingPreparedStatementCallback callback = new AbstractLobCreatingPreparedStatementCallback(lobHandler) {
            @Override
            protected void setValues(PreparedStatement ps, LobCreator creator) throws SQLException, DataAccessException {
                int plainPsLength = plainPreparedStatement.length;

                for (int i = 1; i <= plainPsLength; i++) {
                    ps.setObject(i, plainPreparedStatement[i-1]);
                }

                for (byte[] bytes : bytesPreparedStatement){
                    creator.setBlobAsBytes(ps, ++ plainPsLength, bytes );
                }
            }
        };
        return execute(sql,callback);
    }

    JdbcTemplateAdapter(DataSource ds){
        super(ds);
    }
}
