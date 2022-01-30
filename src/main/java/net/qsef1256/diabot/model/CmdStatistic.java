package net.qsef1256.diabot.model;

import com.jagrosh.jdautilities.command.SlashCommand;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.qsef1256.diabot.data.CmdStatisticEntity;
import net.qsef1256.diabot.database.DaoCommon;
import net.qsef1256.diabot.database.DaoCommonImpl;
import net.qsef1256.diabot.util.LocalDateUtil;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import static net.qsef1256.diabot.DiaBot.logger;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CmdStatistic {

    private CmdStatisticEntity statistic;

    DaoCommon<String, CmdStatisticEntity> dao = new DaoCommonImpl<>(CmdStatisticEntity.class);

    public CmdStatistic(Class<? extends SlashCommand> command) {
        try {
            statistic = dao.findById(command.getSimpleName());
            if (!LocalDateUtil.isToday(getLastUseTime())) {
                statistic.setTodayUsed(0);
            }
            statistic.setUseCount(getUseCount() + 1);
            statistic.setTodayUsed(getTodayUsed() + 1);
            statistic.setLastUseTime(LocalDateTime.now());
            dao.update(statistic);
        } catch (NoSuchElementException e) {
            createStatistic(command);
        } catch (RuntimeException e) {
            logger.warn(e.getMessage());
            throw new RuntimeException("Error when handling " + command.getSimpleName() + "'s statistics");
        }
    }

    private void createStatistic(Class<? extends SlashCommand> command) {
        CmdStatisticEntity data = new CmdStatisticEntity();
        data.setCommandName(command.getSimpleName());
        data.setLastUseTime(LocalDateTime.now());
        data.setUseCount(1);
        data.setTodayUsed(1);
        logger.info("Creating " + command.getSimpleName() + "'s statistics");
        statistic = data;
        dao.create(data);
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
