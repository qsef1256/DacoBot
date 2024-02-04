package net.qsef1256.dacobot.command;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Slf4j
@SpringBootTest
class CreditCommandTest {

    @Test
    void credit(@Autowired BuildProperties buildProperties) {
        assertDoesNotThrow(() -> {
            System.out.println("Project Name: " + buildProperties.getName());
            System.out.println("Project Version: " + buildProperties.getVersion());
        });
    }

}
