package net.qsef1256.dacobot.database;

import lombok.extern.slf4j.Slf4j;
import net.qsef1256.dacobot.module.account.data.UserEntity;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
class UserTest {

    private static final DaoCommonJpa<UserEntity, Long> dao = new DaoCommonJpaImpl<>(UserEntity.class);

    private static final UserEntity testUser = new UserEntity();

    static {
        testUser.setDiscordId(419761037861060620L);
        testUser.setRegisterTime(LocalDateTime.now());
        testUser.setStatus("TEST");
    }

    @Test
    void testUserEntity() {
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
        log.info("User Count: " + dao.count());

        log.info("is Exists?: " + dao.existsById(testUser.getDiscordId()));
        log.info("Before save: " + testUser.getDiscordId().toString());

        dao.save(testUser);
        assertEquals(String.valueOf(419761037861060620L), testUser.getDiscordId().toString());
        log.info("After save: " + testUser.getDiscordId().toString());
        log.info(dao.toString());
    }

    @AfterEach
    void removeTestUser() {
        dao.deleteById(testUser.getDiscordId());
        dao.close();
    }

}
