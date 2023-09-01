package net.qsef1256.dacobot.database;

import net.qsef1256.dacobot.service.account.data.UserEntity;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static net.qsef1256.dacobot.DacoBot.logger;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class JPATest {

    private static final DaoCommonJpa<UserEntity, Long> dao = new DaoCommonJpaImpl<>(UserEntity.class);

    private static final UserEntity testUser = new UserEntity();

    static {
        testUser.setDiscordId(419761037861060620L);
        testUser.setRegisterTime(LocalDateTime.now());
        testUser.setStatus("TEST");
    }

    @Test
    void testJPA() {
        testUser(dao);

        dao.close();
    }

    @Test
    void testAutoClose() {
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 3; i++) {
                try (DaoCommonJpa<UserEntity, Long> dao = new DaoCommonJpaImpl<>(UserEntity.class)) {
                    testUser(dao);
                }
            }
        });
    }

    private static void testUser(@NotNull DaoCommonJpa<UserEntity, Long> dao) {
        logger.info("User Count: " + dao.count());

        logger.info("is Exists?: " + dao.existsById(testUser.getDiscordId()));
        logger.info("Before save: " + testUser.getDiscordId().toString());

        dao.save(testUser);
        assertEquals(String.valueOf(419761037861060620L), testUser.getDiscordId().toString());
        logger.info("After save: " + testUser.getDiscordId().toString());
        logger.info(dao.toString());
    }

    @AfterEach
    void removeTestUser() {
        dao.deleteById(testUser.getDiscordId());
        dao.close();
    }

}
