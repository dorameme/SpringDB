package hello.DB.connection;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static hello.DB.connection.ConnectionConst.*;


@Slf4j
public class DBConnectionUtil {
    public static Connection getConnection()
    {
        try{
        Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);//JDBC에서 제공하는 DriverManager.getConnection()로 데이터베이스 연결
        log.info("get connection={}, class={}", connection, connection.getClass());
        return connection;
        }catch (SQLException e){
            throw new IllegalStateException(e);
        }
    }
}
