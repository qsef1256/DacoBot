package net.qsef1256.dacobot.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import lombok.Getter;
import net.qsef1256.dacobot.module.cmdstat.CmdStatisticService;
import net.qsef1256.dacobot.module.cmdstat.data.CmdStatisticEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 뭐라 적지?
 */
@Component
public abstract class DacoCommand extends SlashCommand {

    private CmdStatisticService statisticService;
    @Getter
    private CmdStatisticEntity cmdStatistic;
    protected boolean statistic = false;

    @Override
    protected final void execute(@NotNull SlashCommandEvent event) {
        if (statistic) cmdStatistic = statisticService.addCmdStatistic(name);

        runCommand(event);
    }

    protected abstract void runCommand(@NotNull SlashCommandEvent event);

    @Autowired
    public void setStatisticService(@NotNull CmdStatisticService statisticService) {
        this.statisticService = statisticService;
    }

}
