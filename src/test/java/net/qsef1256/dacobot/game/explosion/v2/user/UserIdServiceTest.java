package net.qsef1256.dacobot.game.explosion.v2.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class UserIdServiceTest {

    @Autowired
    private UserIdService userId;

    @Test
    void getUserId() {
        assertThrows(IllegalArgumentException.class, () -> userId.getUserId(1L));
    }

}