package net.qsef1256.diabot.game.explosion.model;

import net.qsef1256.diabot.game.explosion.dao.ExplosionDAO;
import net.qsef1256.diabot.game.explosion.dao.ExplosionDAOImpl;
import net.qsef1256.diabot.game.explosion.data.DiscordCashData;
import net.qsef1256.diabot.util.DiscordUtil;

import java.sql.SQLException;

import static net.qsef1256.diabot.DiaBot.logger;

public class ExplosionGameCore {
    protected static final ExplosionDAO dao = new ExplosionDAOImpl();

    public static void register(final long discord_id) throws SQLException {
        try {
            dao.addData(new DiscordCashData(discord_id, 0, 0));
        } catch (final SQLException e) {
            logger.error(e.getMessage());
            throw new SQLException(DiscordUtil.getNameAsTag(discord_id) + " 유저 등록에 실패했습니다.");
        }
    }

    public static void reset(final long discord_id) throws SQLException {
        try {
            dao.resetData(discord_id);
            register(discord_id);
        } catch (final SQLException e) {
            logger.error(e.getMessage());
            throw new SQLException(DiscordUtil.getNameAsTag(discord_id) + " 초기화에 실패했습니다.");
        }
    }

}
