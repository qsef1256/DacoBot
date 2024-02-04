package net.qsef1256.dacobot.module.cmdstat;

import net.qsef1256.dacobot.module.cmdstat.data.CmdStatisticEntity;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CmdStatisticServiceTest {

    @BeforeEach
    void resetStatistic(@Autowired CmdStatisticService service) {
        service.removeCmdStatistic("TestCommand");
    }

    @AfterAll
    static void clearStatistic(@Autowired CmdStatisticService service) {
        service.removeCmdStatistic("TestCommand");
    }

    @Test
    void getCmdStatistic(@Autowired CmdStatisticService service) {
        CmdStatisticEntity statistic = service.getCmdStatistic("TestCommand");

        assertEquals(0, statistic.getUseCount());
        assertEquals(0, statistic.getTodayUsed());
        assertEquals("TestCommand", statistic.getCommandName());
    }

    @Test
    void addCmdStatistic(@Autowired CmdStatisticService service) {
        CmdStatisticEntity statistic = service.addCmdStatistic("TestCommand");

        assertEquals(1, statistic.getUseCount());
        assertEquals(1, statistic.getTodayUsed());
        assertTrue(statistic.getLastUseTime()
                .toLocalDate()
                .isEqual(LocalDate.now()));

        CmdStatisticEntity updatedStatistic = service.addCmdStatistic("TestCommand");

        assertEquals(2, updatedStatistic.getUseCount());
        assertEquals(2, updatedStatistic.getTodayUsed());
    }

    @Test
    void getUseInfo(@Autowired CmdStatisticService service) {
        service.addCmdStatistic("TestCommand");
        service.addCmdStatistic("TestCommand");

        assertEquals("금일: 2 총합: 2", service.getUseInfo("TestCommand"));
    }

    @Test
    void asdfCommand(@Autowired CmdStatisticService service) {
        service.addCmdStatistic("TestCommand");

        assertEquals("금일: 1 총합: 1", service.getUseInfo("TestCommand"));
    }

}