package net.qsef1256.dacobot.module.account.user;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@SpringBootTest
class UserTest {

    private static final UserEntity testUser = new UserEntity();

    static {
        testUser.setDiscordId(419761037861060620L);
        testUser.setRegisterTime(LocalDateTime.now());
        testUser.setStatus("TEST");
    }

    @Test
    void testUserEntity(@Autowired UserRepository repository) {
        testUser(repository);
    }

    @Test
    void testAutoClose(@Autowired UserRepository repository) {
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 3; i++) {
                testUser(repository);
            }
        });
    }

    private static void testUser(@NotNull UserRepository repository) {
        log.info("User Count: " + repository.count());
        log.info("is Exists?: " + repository.existsById(testUser.getDiscordId()));
        log.info("Before save: " + testUser.getDiscordId().toString());

        repository.save(testUser);
        assertEquals(String.valueOf(419761037861060620L), testUser.getDiscordId().toString());
        log.info("After save: " + testUser.getDiscordId().toString());
        log.info(repository.toString());
    }

    @AfterEach
    void removeTestUser(@Autowired UserRepository repository) {
        repository.deleteById(testUser.getDiscordId());
    }

}
