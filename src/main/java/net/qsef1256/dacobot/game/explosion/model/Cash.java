package net.qsef1256.dacobot.game.explosion.model;

import lombok.Getter;
import net.qsef1256.dacobot.database.DaoCommon;
import net.qsef1256.dacobot.database.DaoCommonImpl;
import net.qsef1256.dacobot.game.explosion.data.CashEntity;
import net.qsef1256.dacobot.system.account.data.AccountEntity;
import net.qsef1256.dacobot.util.DiscordUtil;

import static net.qsef1256.dacobot.DacoBot.logger;

public class Cash {

    protected final DaoCommon<Long, AccountEntity> userDao = new DaoCommonImpl<>(AccountEntity.class);
    protected final DaoCommon<Long, CashEntity> cashDao = new DaoCommonImpl<>(CashEntity.class);
    @Getter
    private final CashEntity data;

    public Cash(final long discord_id) {
        try {
            data = userDao.findById(discord_id).getExplosionCash();
        } catch (RuntimeException e) {
            logger.warn(e.getMessage());
            throw new RuntimeException(DiscordUtil.getNameAsTag(discord_id) + "님의 계정 캐시를 로드하는데 실패했습니다.");
        }
    }

    public long getCash() {
        return data.getCash();
    }

    public void addCash(final int amount) {
        data.setCash(getCash() + amount);
        if (data.getCash() < 0) {
            data.setCash(0L);
        }
        cashDao.update(data);
    }

    public int getPickaxeCount() {
        return data.getPickaxeCount();
    }

    public void addPickaxeCount(final int count) {
        data.setPickaxeCount(getPickaxeCount() + count);
        if (data.getPickaxeCount() < 0) {
            data.setPickaxeCount(0);
        }
        cashDao.update(data);
    }

    public void addPickaxeCount() {
        addPickaxeCount(1);
    }

}
