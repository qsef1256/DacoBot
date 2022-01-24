package net.qsef1256.diabot.game.explosion.model;

import com.sun.jdi.request.DuplicateRequestException;
import net.qsef1256.diabot.data.DiscordUserData;
import net.qsef1256.diabot.database.DaoCommon;
import net.qsef1256.diabot.database.DaoCommonImpl;
import net.qsef1256.diabot.game.explosion.data.ExplosionCash;
import net.qsef1256.diabot.util.DiscordUtil;

import java.util.NoSuchElementException;

import static net.qsef1256.diabot.DiaBot.logger;

public class ExplosionGameCore {
    protected static final DaoCommon<Long, DiscordUserData> mainDao = new DaoCommonImpl<>(DiscordUserData.class);
    protected static final DaoCommon<Long, ExplosionCash> explosionDAO = new DaoCommonImpl<>(ExplosionCash.class);

    public static void register(final long discord_id) {
        try {
            if (!mainDao.isExist(discord_id))
                throw new NoSuchElementException(DiscordUtil.getNameAsTag(discord_id) + " 유저가 존재하지 않습니다.");
            ExplosionCash cashData = new ExplosionCash();
            cashData.setDiscord_user(mainDao.findById(discord_id));
            cashData.setCash(0L);
            cashData.setPickaxeCount(0);
            explosionDAO.create(cashData);
        } catch (NoSuchElementException e) {
            throw e;
        } catch (final RuntimeException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(DiscordUtil.getNameAsTag(discord_id) + " 데이터 등록에 실패했습니다.");
        }
    }

    public static void reset(final long discord_id) {
        try {
            explosionDAO.deleteById(discord_id);
            register(discord_id);
        } catch (final RuntimeException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(DiscordUtil.getNameAsTag(discord_id) + " 초기화에 실패했습니다.");
        }
    }

    public static void delete(final long discord_id) {
        try {
            if (!mainDao.isExist(discord_id))
                throw new DuplicateRequestException(DiscordUtil.getNameAsTag(discord_id) + " 계정은 이미 삭제 되었습니다.");
            mainDao.deleteById(discord_id);
        } catch (DuplicateRequestException e) {
            throw e;
        } catch (final RuntimeException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(DiscordUtil.getNameAsTag(discord_id) + " 계정 삭제에 실패했습니다.");
        }
    }
}
