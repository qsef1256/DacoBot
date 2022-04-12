package net.qsef1256.dacobot.game.explosion.model;

import lombok.Getter;
import net.qsef1256.dacobot.database.DaoCommonJpa;
import net.qsef1256.dacobot.database.DaoCommonJpaImpl;
import net.qsef1256.dacobot.game.explosion.data.CashEntity;
import net.qsef1256.dacobot.system.account.data.AccountEntity;
import net.qsef1256.dacobot.util.JDAUtil;

import static net.qsef1256.dacobot.DacoBot.logger;

public class Cash {

    protected static final DaoCommonJpa<AccountEntity, Long> userDao = new DaoCommonJpaImpl<>(AccountEntity.class);
    protected static final DaoCommonJpa<CashEntity, Long> cashDao = new DaoCommonJpaImpl<>(CashEntity.class);
    @Getter
    private final CashEntity data;

    // TODO: fix lazy initialization like Inventory
    // TODO: fix cash cached problem (update required)
    // TODO: find where cached data exists (1st? 2st? or else?) > 1st로 추정
    // TODO: https://stackoverflow.com/questions/13258976/how-to-refresh-jpa-entities-when-backend-database-changes-asynchronously
    // TODO: http://ldg.pe.kr/framework_reference/hibernate/ver3.x/html/transactions.html
    public Cash(final long discordId) {
        userDao.open();
        cashDao.open();

        try {
            data = userDao.findById(discordId).getExplosionCash();
        } catch (RuntimeException e) {
            logger.warn(e.getMessage());
            throw new RuntimeException(JDAUtil.getNameAsTag(discordId) + "님의 계정 캐시를 로드하는데 실패했습니다.");
        }

        userDao.close();
        cashDao.close();
    }

    public long getCash() {
        return data.getCash();
    }

    public void addCash(final int amount) {
        data.setCash(getCash() + amount);
        if (data.getCash() < 0) {
            data.setCash(0L);
        }
        cashDao.saveAndClose(data);
    }

    public int getPickaxeCount() {
        return data.getPickaxeCount();
    }

    public void addPickaxeCount(final int count) {
        data.setPickaxeCount(getPickaxeCount() + count);
        if (data.getPickaxeCount() < 0)
            data.setPickaxeCount(0);

        cashDao.saveAndClose(data);
    }

    public void addPickaxeCount() {
        addPickaxeCount(1);
    }

}
