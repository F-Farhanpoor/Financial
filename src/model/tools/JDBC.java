package model.tools;

import lombok.Getter;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class JDBC {
    @Getter
private static JDBC jdbc = new JDBC();
    private static BasicDataSource basicDataSource = new BasicDataSource();

    private JDBC() {
    }

    public Connection getConnection() throws SQLException {
        basicDataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        basicDataSource.setUrl("jdbc:oracle:thin:@localhost:1521:xe");
        basicDataSource.setUsername("java");
        basicDataSource.setPassword("java123");
        basicDataSource.setMinIdle(5);
        basicDataSource.setMaxTotal(20);
        return basicDataSource.getConnection();
    }
}
