package net.qsef1256.diabot.game.explosion.model;

import lombok.Getter;
import net.qsef1256.diabot.game.explosion.dao.ExplosionDAO;
import net.qsef1256.diabot.game.explosion.dao.ExplosionDAOImpl;
import net.qsef1256.diabot.game.explosion.data.DiscordCashData;
import net.qsef1256.diabot.model.DiscordUser;

import java.sql.SQLException;

public class ExplosionUser {

    protected final ExplosionDAO dao = new ExplosionDAOImpl();
    private final DiscordUser user;
    @Getter
    private final DiscordCashData record;

    public ExplosionUser(final long discord_id) throws SQLException {
        user = new DiscordUser(discord_id);
        record = dao.getData(discord_id);
    }

    public long getCash() {
        return record.getCash();
    }

    public int getPickaxeCount() {
        return record.getPickaxe_count();
    }

    public void addPickaxeCount(final int count) throws SQLException {
        record.setPickaxe_count(getPickaxeCount() + count);
        dao.updateData(record);
    }

    public void addPickaxeCount() throws SQLException {
        addPickaxeCount(1);
    }

}
