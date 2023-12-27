package net.qsef1256.dacobot.module.cmdstat;

import net.qsef1256.dacobot.module.cmdstat.data.CmdStatisticEntity;
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

        service.removeCmdStatistic("TestCommand");
    }

}