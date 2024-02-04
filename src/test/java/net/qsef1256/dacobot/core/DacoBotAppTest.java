package net.qsef1256.dacobot.core;

import net.qsef1256.dacobot.DacoBot;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class DacoBotAppTest {

    @Test
    void testDacoBot(@Autowired DacoBot dacoBot) {
        assertNotNull(dacoBot);
    }

}