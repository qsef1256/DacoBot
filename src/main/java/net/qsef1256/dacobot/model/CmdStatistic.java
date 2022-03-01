package net.qsef1256.dacobot.model;

import com.jagrosh.jdautilities.command.SlashCommand;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.qsef1256.dacobot.data.CmdStatisticEntity;
import net.qsef1256.dacobot.database.DaoCommon;
import net.qsef1256.dacobot.database.DaoCommonImpl;
import net.qsef1256.dacobot.util.LocalDateTimeUtil;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import static net.qsef1256.dacobot.DacoBot.logger;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CmdStatistic {

    private CmdStatisticEntity statistic;

    final DaoCommon<CmdStatisticEntity, String> dao = new DaoCommonImpl<>(CmdStatisticEntity.class);

    public CmdStatistic(Class<? extends SlashCommand> command) {
        try {
            statistic = dao.findById(command.getSimpleName());
            if (!LocalDateTimeUtil.isToday(getLastUseTime())) {
                statistic.setTodayUsed(0);
            }
            statistic.setUseCount(getUseCount() + 1);
            statistic.setTodayUsed(getTodayUsed() + 1);
            statistic.setLastUseTime(LocalDateTime.now());
            dao.save(statistic);
        } catch (NoSuchElementException e) {
            createStatistic(command);
        } catch (RuntimeException e) {
            logger.warn(e.getMessage());
            throw new RuntimeException("Error when handling " + command.getSimpleName() + "'s statistics");
        }
    }

    private void createStatistic(@NotNull Class<? extends SlashCommand> command) {
        CmdStatisticEntity data = new CmdStatisticEntity();
        data.setCommandName(command.getSimpleName());
        data.setLastUseTime(LocalDateTime.now());
        data.setUseCount(1);
        data.setTodayUsed(1);
        logger.info("Creating " + command.getSimpleName() + "'s statistics");
        statistic = data;
        dao.save(data);
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
