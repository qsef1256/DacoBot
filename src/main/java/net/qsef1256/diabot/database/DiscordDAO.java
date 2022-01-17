package net.qsef1256.diabot.database;

public interface DiscordDAO {

    void init();
    boolean isTableExist(String table);

}
