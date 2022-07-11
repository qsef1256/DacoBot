package net.qsef1256.dacobot.game.explosion.model;

import jakarta.persistence.NoResultException;
import lombok.Getter;
import net.qsef1256.dacobot.database.DaoCommonJpa;
import net.qsef1256.dacobot.database.DaoCommonJpaImpl;
import net.qsef1256.dacobot.database.JpaManager;
import net.qsef1256.dacobot.game.explosion.data.CashEntity;
import net.qsef1256.dacobot.service.account.data.AccountEntity;
import net.qsef1256.dacobot.service.account.model.AccountManager;

import static net.qsef1256.dacobot.DacoBot.logger;

public class Cash {

    protected static final DaoCommonJpa<CashEntity, Long> dao = new DaoCommonJpaImpl<>(CashEntity.class);
    @Getter
    private CashEntity data;

    // FIXME: fix lazy initialization like Inventory
    // FIXME: fix cash cached problem (update required)
    // TODO: find where cached data exists (1st? 2st? or else?) > 1st로 추정
    // TODO: https://stackoverflow.com/questions/13258976/how-to-refresh-jpa-entities-when-backend-database-changes-asynchronously
    // TODO: http://ldg.pe.kr/framework_reference/hibernate/ver3.x/html/transactions.html
    public Cash(final long discordId) {
        dao.open();

        AccountEntity account;
        try {
            account = (AccountEntity) JpaManager.getEntityManager()
                    .createQuery("select m from AccountEntity m join fetch m.explosionCash where m.discordId = :discordId")
                    .setParameter("discordId", discordId)
                    .getSingleResult();
            data = account.getExplosionCash();
        } catch (NoResultException e) {
            logger.info("creating Cash for %s".formatted(discordId));

            account = AccountManager.getAccount(discordId);
            data = new CashEntity().setDiscordUser(account);
            dao.saveAndClose(data);
            return;
        }

        dao.close();
    }

    public long getCash() {
        return data.getCash();
    }

    public void addCash(final int amount) {
        data.setCash(getCash() + amount);
        if (data.getCash() < 0) {
            data.setCash(0L);
        }
        saveAndClose();
    }

    public int getPickaxeCount() {
        return data.getPickaxeCount();
    }

    public void addPickaxeCount(final int count) {
        data.setPickaxeCount(getPickaxeCount() + count);
        if (data.getPickaxeCount() < 0)
            data.setPickaxeCount(0);

        saveAndClose();
    }

    public void addPickaxeCount() {
        addPickaxeCount(1);
    }

    private void saveAndClose() {
        dao.saveAndClose(data);
    }

}
