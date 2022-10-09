package net.qsef1256.dacobot.database;

import net.qsef1256.dacobot.service.account.data.AccountEntity;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static net.qsef1256.dacobot.DacoBot.logger;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class HibernateTest {

    public void DaoCheck() {
        DaoCommonJpa<AccountEntity, Long> dao = new DaoCommonJpaImpl<>(AccountEntity.class);

        logger.info("qsef1256 is exist?: " + dao.existsById(419761037861060619L));
        if (!dao.existsById(419761037861060620L))
            save(dao);
        find(dao);
        dao.deleteById(419761037861060620L);
        long count = dao.count();

        logger.info("count: " + count);
    }

    private void find(@NotNull DaoCommon<AccountEntity, Long> dao) {
        AccountEntity user = dao.findById(419761037861060620L);
        logger.info(user.getDiscordId() + " Status: " + user.getStatus() + " Time: " + user.getRegisterTime());
    }

    private void save(@NotNull DaoCommon<AccountEntity, Long> dao) {
        AccountEntity testUser = new AccountEntity();
        testUser.setDiscordId(419761037861060620L);
        testUser.setRegisterTime(LocalDateTime.now());
        testUser.setStatus("TEST");

        logger.info("Creating user: " + testUser.getDiscordId());
        dao.save(testUser);
    }

    @Test
    void testHibernate() {
        assertDoesNotThrow(this::DaoCheck);
    }

}
