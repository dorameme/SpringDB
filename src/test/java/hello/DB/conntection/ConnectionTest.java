package hello.DB.conntection;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static hello.DB.connection.ConnectionConst.*;

@Slf4j
public class ConnectionTest {
    @Test
    void driverManager() throws SQLException {
        Connection con1= DriverManager.getConnection(URL,USERNAME,PASSWORD);
        Connection con2= DriverManager.getConnection(URL,USERNAME,PASSWORD);
        log.info("conneciton={}, class={}",con1,con1.getClass());
        log.info("conneciton={}, class={}",con2,con2.getClass());
    }

    @Test
    void  dataSourceDriverManager() throws SQLException{
        DriverManagerDataSource dataSource=new DriverManagerDataSource(URL,USERNAME,PASSWORD);
        useDataSource(dataSource);
    }

    @Test
    void datSourceConnectionPool() throws SQLException ,InterruptedException{
        HikariDataSource dataSource=new HikariDataSource();
        dataSource.setPoolName("MyPool");
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setMaximumPoolSize(10);

        useDataSource(dataSource);
        Thread.sleep(1000);
    }
    private void useDataSource(DataSource dataSource) throws SQLException{
        Connection con1=dataSource.getConnection();
        Connection con2= dataSource.getConnection();
        log.info("conneciton={}, class={}",con1,con1.getClass());
        log.info("conneciton={}, class={}",con2,con2.getClass());
    }
}
