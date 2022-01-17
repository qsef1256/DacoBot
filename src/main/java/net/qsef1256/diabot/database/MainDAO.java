package net.qsef1256.diabot.database;

import net.qsef1256.diabot.data.DiscordUserData;

import java.sql.SQLException;

public interface MainDAO {

    void init();

    boolean isTableExist(String table) throws SQLException;

    boolean isUserExist(long discord_id) throws SQLException;

    DiscordUserData getUser(long discord_id) throws SQLException;

    void addUser(DiscordUserData user) throws SQLException;

}
