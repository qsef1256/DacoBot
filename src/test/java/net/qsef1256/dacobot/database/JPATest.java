package net.qsef1256.dacobot.database;

import net.qsef1256.dacobot.service.account.data.AccountEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static net.qsef1256.dacobot.DacoBot.logger;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JPATest {

    private static final DaoCommonJpa<AccountEntity, Long> dao = new DaoCommonJpaImpl<>(AccountEntity.class);

    @Test
    void testJPA() {
        logger.info("User Count: " + dao.count());

        final AccountEntity testUser = new AccountEntity();
        testUser.setDiscordId(419761037861060620L);
        testUser.setRegisterTime(LocalDateTime.now());
        testUser.setStatus("TEST");

        logger.info("is Exists?: " + dao.existsById(testUser.getDiscordId()));
        logger.info("Before save: " + testUser.getDiscordId().toString());

        dao.save(testUser);
        assertEquals(String.valueOf(419761037861060620L), testUser.getDiscordId().toString());
        logger.info("After save: " + testUser.getDiscordId().toString());
        logger.info(dao.toString());

        dao.close();
    }

}
