package net.qsef1256.diabot.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HikariPoolManager {

    @Getter
    private static final HikariPoolManager instance;
    private final HikariDataSource dataSource;

    private static final Logger log = LoggerFactory.getLogger(HikariPoolManager.class.getSimpleName());
    private static final HikariConfig config = new HikariConfig();

    private String hostname;
    private int port;
    private String database;
    private String username;
    private String password;

    static {
        instance = new HikariPoolManager();
    }

    private HikariPoolManager() {
        setJdbcInfo();

        config.setJdbcUrl(
                "jdbc:mysql://" + hostname + ":" + port + "/" + database
        );

        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setUsername(username);
        config.setPassword(password);
        dataSource = new HikariDataSource(config);
    }

    private void setJdbcInfo() {
        hostname = "localhost";
        port = 3306;
        database = "diabot";
        username = "root";
        password = "1234";
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void close(Connection conn, PreparedStatement ps, ResultSet res) {
        if (conn != null) try { conn.close(); } catch (SQLException ignored) {}
        if (ps != null) try { ps.close(); } catch (SQLException ignored) {}
        if (res != null) try { res.close(); } catch (SQLException ignored) {}
    }

}
