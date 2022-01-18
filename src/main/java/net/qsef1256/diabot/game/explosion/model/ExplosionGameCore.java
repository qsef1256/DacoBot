package net.qsef1256.diabot.game.explosion.model;

import com.sun.jdi.request.DuplicateRequestException;
import net.qsef1256.diabot.database.MainDAO;
import net.qsef1256.diabot.database.MainDAOImpl;
import net.qsef1256.diabot.game.explosion.dao.ExplosionDAO;
import net.qsef1256.diabot.game.explosion.dao.ExplosionDAOImpl;
import net.qsef1256.diabot.game.explosion.data.DiscordCashData;
import net.qsef1256.diabot.util.DiscordUtil;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import static net.qsef1256.diabot.DiaBot.logger;

public class ExplosionGameCore {
    protected static final MainDAO mainDao = new MainDAOImpl();
    protected static final ExplosionDAO explosionDAO = new ExplosionDAOImpl();

    public static void register(final long discord_id) throws SQLException {
        try {
            if (!mainDao.isUserExist(discord_id))
                throw new NoSuchElementException(DiscordUtil.getNameAsTag(discord_id) + " 유저가 존재하지 않습니다.");
            explosionDAO.addData(new DiscordCashData(discord_id, 0, 0));
        } catch (final SQLException e) {
            logger.error(e.getMessage());
            throw new SQLException(DiscordUtil.getNameAsTag(discord_id) + " 데이터 등록에 실패했습니다.");
        }
    }

    public static void reset(final long discord_id) throws SQLException {
        try {
            explosionDAO.deleteData(discord_id);
            register(discord_id);
        } catch (final SQLException e) {
            logger.error(e.getMessage());
            throw new SQLException(DiscordUtil.getNameAsTag(discord_id) + " 초기화에 실패했습니다.");
        }
    }

    public static void delete(final long discord_id) throws SQLException {
        try {
            if (!mainDao.isUserExist(discord_id))
                throw new DuplicateRequestException(DiscordUtil.getNameAsTag(discord_id) + " 계정은 이미 삭제 되었습니다.");
            mainDao.removeUser(discord_id);
        } catch (final SQLException e) {
            logger.error(e.getMessage());
            throw new SQLException(DiscordUtil.getNameAsTag(discord_id) + " 계정 삭제에 실패했습니다.");
        }
    }
}
