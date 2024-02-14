package net.qsef1256.dacobot.command.fun;

import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.qsef1256.dacobot.command.DacoCommand;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class ChaosCommand extends DacoCommand {

    public ChaosCommand() {
        name = "chaos";
        help = "for science";

        statistic = true;
    }

    @Override
    protected void runCommand(@NotNull SlashCommandEvent event) {
        event.reply("혼돈! 파괴! 망각!").queue();


    }

}
