package net.qsef1256.dacobot.module.cmdstat;

import com.jagrosh.jdautilities.command.SlashCommand;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.qsef1256.dacobot.database.DaoCommonJpa;
import net.qsef1256.dacobot.database.DaoCommonJpaImpl;
import net.qsef1256.dacobot.module.cmdstat.data.CmdStatisticEntity;
import net.qsef1256.dialib.util.LocalDateTimeUtil;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Deprecated(since = "use CmdStatisticService", forRemoval = true)
public class CmdStatistic {

    private final DaoCommonJpa<CmdStatisticEntity, String> dao = new DaoCommonJpaImpl<>(CmdStatisticEntity.class);
    private CmdStatisticEntity statistic;

    public CmdStatistic(Class<? extends SlashCommand> command) {
        try {
            dao.open();

            CmdStatisticEntity data = dao.findById(command.getSimpleName());
            if (data == null) {
                createStatistic(command);
                return;
            }

            statistic = data;
            if (!LocalDateTimeUtil.isToday(getLastUseTime())) {
                statistic.setTodayUsed(0);
            }
            statistic.setUseCount(getUseCount() + 1);
            statistic.setTodayUsed(getTodayUsed() + 1);
            statistic.setLastUseTime(LocalDateTime.now());

            dao.saveAndClose(statistic);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error when handling " + command.getSimpleName() + "'s statistics", e);
        }
    }

    private void createStatistic(@NotNull Class<? extends SlashCommand> command) {
        CmdStatisticEntity data = new CmdStatisticEntity();
        data.setCommandName(command.getSimpleName());
        data.setLastUseTime(LocalDateTime.now());
        data.setUseCount(1);
        data.setTodayUsed(1);

        log.info("Creating %s's statistics".formatted(command.getSimpleName()));
        statistic = data;
        dao.saveAndClose(data);
    }

    public int getUseCount() {
        return statistic.getUseCount();
    }

    public int getTodayUsed() {
        return statistic.getTodayUsed();
    }

    public LocalDateTime getLastUseTime() {
        return statistic.getLastUseTime();
    }

    public String getUseInfo() {
        return "금일: " + getTodayUsed() + " 총합: " + getUseCount();
    }

}
