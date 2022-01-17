package net.qsef1256.diabot.game.explosion.dao;

import net.qsef1256.diabot.game.explosion.data.DiscordCashData;

import java.sql.SQLException;

public interface ExplosionDAO {
    void init();

    DiscordCashData getData(long discord_id) throws SQLException;

    void updateData(DiscordCashData cash) throws SQLException;

    void addData(DiscordCashData cash) throws SQLException;

    void resetData(long discord_id) throws SQLException;
}
