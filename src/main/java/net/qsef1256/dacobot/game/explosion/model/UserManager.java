package net.qsef1256.dacobot.game.explosion.model;

import net.qsef1256.dacobot.database.DaoCommon;
import net.qsef1256.dacobot.database.DaoCommonImpl;
import net.qsef1256.dacobot.game.explosion.data.CashEntity;
import net.qsef1256.dacobot.system.account.data.AccountEntity;
import net.qsef1256.dacobot.util.DiscordUtil;

import java.util.NoSuchElementException;

import static net.qsef1256.dacobot.DacoBot.logger;

public class UserManager {
    protected static final DaoCommon<AccountEntity, Long> mainDao = new DaoCommonImpl<>(AccountEntity.class);
    protected static final DaoCommon<CashEntity, Long> cashDao = new DaoCommonImpl<>(CashEntity.class);

    public static void register(final long discord_id) {
        try {
            if (!mainDao.existsById(discord_id))
                throw new NoSuchElementException(DiscordUtil.getNameAsTag(discord_id) + " 유저가 존재하지 않습니다.");
            CashEntity cashData = new CashEntity();
            cashData.setDiscord_user(mainDao.findById(discord_id));
            cashData.setCash(0L);
            cashData.setPickaxeCount(0);
            cashData.setPrestigeCount(0);
            cashDao.create(cashData);
        } catch (NoSuchElementException e) {
            throw e;
        } catch (final RuntimeException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(DiscordUtil.getNameAsTag(discord_id) + " 데이터 등록에 실패했습니다.");
        }
    }

    public static void reset(final long discord_id) {
        try {
            cashDao.deleteById(discord_id);
            register(discord_id);
        } catch (final RuntimeException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(DiscordUtil.getNameAsTag(discord_id) + " 초기화에 실패했습니다.");
        }
    }

    public static boolean isExist(long discord_id) {
        return cashDao.existsById(discord_id);
    }

}
