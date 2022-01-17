package net.qsef1256.diabot.database;

import net.qsef1256.diabot.data.DiscordUserData;
import org.jetbrains.annotations.NotNull;

import java.sql.*;

import static net.qsef1256.diabot.DiaBot.logger;

public class MainDAOImpl implements MainDAO {

    @Override
    public void init() {
        logger.info("Checking for tables...");
        isTableExist("discord_user");
    }

    @Override
    public boolean isTableExist(final String table) {
        final String query = "SHOW TABLES LIKE ?";
        try (Connection conn = HikariPoolManager.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, table);
            final ResultSet rs = ps.executeQuery();

            return rs.next();
        } catch (final SQLException e) {
            logger.error("Failed to check MySQL table: " + table + ", SQL State: " + e.getSQLState());
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean isUserExist(final long discord_id) throws SQLException {
        try {
            if (getUser(discord_id).getRegister_time() != null) return true;
        } catch (final NullPointerException ignored) {
        }
        return false;
    }

    @Override
    public DiscordUserData getUser(final long discord_id) throws SQLException {
        final DiscordUserData data;

        final String query = "SELECT * FROM discord_user WHERE discord_id = ?";
        try (Connection conn = HikariPoolManager.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setLong(1, discord_id);
            data = executeGetQuery(ps);
        }
        return data;
    }

    private DiscordUserData executeGetQuery(@NotNull final PreparedStatement ps) throws SQLException {
        final ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            final long discord_id = rs.getLong("discord_id");
            final Timestamp register_time = rs.getTimestamp("register_time");
            final String status = rs.getString("status");
            return new DiscordUserData(discord_id, register_time, status);
        }
        return null;
    }

    @Override
    public void addUser(final DiscordUserData user) throws SQLException {
        final String query = "INSERT INTO discord_user (discord_id, register_time, status) VALUES (?,?,?)";
        try (Connection conn = HikariPoolManager.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setLong(1, user.getDiscord_id());
            ps.setTimestamp(2, user.getRegister_time());
            ps.setString(3, user.getStatus());
            ps.executeUpdate();
        }
    }
}
