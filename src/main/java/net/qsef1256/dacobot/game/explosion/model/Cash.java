package net.qsef1256.dacobot.game.explosion.model;

import jakarta.persistence.NoResultException;
import lombok.Getter;
import net.qsef1256.dacobot.database.DaoCommonJpa;
import net.qsef1256.dacobot.database.DaoCommonJpaImpl;
import net.qsef1256.dacobot.database.JpaController;
import net.qsef1256.dacobot.game.explosion.data.CashEntity;
import net.qsef1256.dacobot.service.account.controller.AccountController;
import net.qsef1256.dacobot.service.account.data.UserEntity;

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

        UserEntity account;
        try {
            account = (UserEntity) JpaController.getEntityManager()
                    .createQuery("select m from UserEntity m join fetch m.explosionCash where m.discordId = :discordId")
                    .setParameter("discordId", discordId)
                    .getSingleResult();
            data = account.getExplosionCash();
        } catch (NoResultException e) {
            logger.info("creating Cash for %s".formatted(discordId));

            account = AccountController.getAccount(discordId);
            data = new CashEntity().setDiscordUser(account);
            dao.save(data);
        } finally {
            dao.close();
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
        saveAndClose();
    }

    public int getPickaxeCount() {
        return data.getPickaxeCount();
    }

    public void changePickaxeCount(final int count) {
        data.setPickaxeCount(getPickaxeCount() + count);
        if (data.getPickaxeCount() < 0)
            data.setPickaxeCount(0);
        saveAndClose();
    }

    public void addPickaxeCount() {
        changePickaxeCount(1);
    }

    private void saveAndClose() { // TODO: is good?
        dao.open();
        dao.saveAndClose(data);
    }

}
