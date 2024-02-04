package net.qsef1256.dacobot.module.cmdstat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.qsef1256.dacobot.module.cmdstat.data.CmdStatisticEntity;
import net.qsef1256.dialib.util.LocalDateTimeUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class CmdStatisticService {

    private final CmdStatisticRepository repository;

    public CmdStatisticEntity getCmdStatistic(@NotNull String name) {
        return repository
                .findById(name)
                .orElseGet(() -> createCmdStatistic(name));
    }

    public String getUseInfo(@NotNull String name) {
        CmdStatisticEntity statistic = getCmdStatistic(name);

        return "금일: " + statistic.getTodayUsed() + " 총합: " + statistic.getUseCount();
    }

    public CmdStatisticEntity addCmdStatistic(@NotNull String name) {
        CmdStatisticEntity statistic = getCmdStatistic(name);

        if (!LocalDateTimeUtil.isToday(statistic.getLastUseTime()))
            statistic.setTodayUsed(0);

        statistic.setUseCount(statistic.getUseCount() + 1);
        statistic.setTodayUsed(statistic.getTodayUsed() + 1);
        statistic.setLastUseTime(LocalDateTime.now());

        repository.saveAndFlush(statistic);
        return statistic;
    }

    @NotNull
    private CmdStatisticEntity createCmdStatistic(@NotNull String name) {
        log.info("Creating %s's statistics".formatted(name));

        CmdStatisticEntity cmdStatisticEntity = new CmdStatisticEntity();
        cmdStatisticEntity.setCommandName(name);
        cmdStatisticEntity.setLastUseTime(LocalDateTime.now());

        repository.saveAndFlush(cmdStatisticEntity);
        return cmdStatisticEntity;
    }

    public void removeCmdStatistic(@NotNull String name) {
        repository.deleteById(name);
    }

}
