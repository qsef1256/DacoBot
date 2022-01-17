package net.qsef1256.diabot.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static net.qsef1256.diabot.DiaBot.logger;

public class MySQLDAO implements DiscordDAO {

    @Override
    public void init() {
        logger.info("Check for table 'discord'...");

        String query = "CREATE TABLE IF NOT EXISTS user ("
                + "discord_id      int(20) not null primary key,"
                + "register_time   timestamp,"
                + "status          varchar(255) default 'OK' not null"
                + ")";
        try (Connection conn = HikariPoolManager.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.execute();
        } catch (SQLException e) {
            logger.error("Failed to load MySQL database: " + e.getSQLState());
            e.printStackTrace();
        }
    }


    @Override
    public boolean isTableExist(String table) {
        String query = "SHOW TABLES LIKE ?";
        try (Connection conn = HikariPoolManager.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, table);
            ResultSet rs = ps.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
