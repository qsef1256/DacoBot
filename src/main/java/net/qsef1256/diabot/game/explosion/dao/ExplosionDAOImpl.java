package net.qsef1256.diabot.game.explosion.dao;

import net.qsef1256.diabot.database.HikariPoolManager;
import net.qsef1256.diabot.database.MainDAOImpl;
import net.qsef1256.diabot.game.explosion.data.DiscordCashData;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExplosionDAOImpl implements ExplosionDAO {

    @Override
    public void init() {
        new MainDAOImpl().isTableExist("explosion_cash");
    }

    @Override
    public DiscordCashData getData(final long discord_id) throws SQLException {
        final DiscordCashData data;

        final String query = "SELECT * FROM explosion_cash WHERE discord_id = ?";
        try (Connection conn = HikariPoolManager.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setLong(1, discord_id);
            data = executeGetQuery(ps);
        }
        return data;
    }

    private DiscordCashData executeGetQuery(@NotNull final PreparedStatement ps) throws SQLException {
        final ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            final long discord_id = rs.getLong("discord_id");
            final long cash = rs.getLong("cash");
            final int pickaxe_count = rs.getInt("pickaxe_count");
            return new DiscordCashData(discord_id, cash, pickaxe_count);
        }
        return null;
    }

    @Override
    public void updateData(final DiscordCashData cashData) throws SQLException {
        final String query = "UPDATE explosion_cash SET cash = ?, pickaxe_count = ? WHERE discord_id = ?";
        try (Connection conn = HikariPoolManager.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setLong(1, cashData.getCash());
            ps.setInt(2, cashData.getPickaxe_count());
            ps.setLong(3, cashData.getDiscord_id());
            ps.executeUpdate();
        }
    }

    @Override
    public void addData(final DiscordCashData cashData) throws SQLException {
        final String query = "INSERT INTO explosion_cash (discord_id, cash) VALUES (?,?)";
        try (Connection conn = HikariPoolManager.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setLong(1, cashData.getDiscord_id());
            ps.setLong(2, cashData.getCash());
            ps.executeUpdate();
        }
    }

    @Override
    public void resetData(final long discord_id) throws SQLException {
        final String query = "DELETE FROM explosion_cash WHERE discord_id = ?";
        try (Connection conn = HikariPoolManager.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setLong(1, discord_id);
            ps.executeUpdate();
        }
    }

}
