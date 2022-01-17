package net.qsef1256.diabot.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static net.qsef1256.diabot.DiaBot.logger;

public class MySQLDAO implements DiscordDAO {

    @Override
    public void init() {
        logger.info("Checking for tables...");
        isTableExist("user");
        isTableExist("cash");
    }

    @Override
    public boolean isTableExist(String table) {
        String query = "SHOW TABLES LIKE ?";
        try (Connection conn = HikariPoolManager.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, table);
            ResultSet rs = ps.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            logger.error("Failed to check MySQL table: " + table + ", SQL State: " + e.getSQLState());
            e.printStackTrace();
        }
        return false;
    }

}
