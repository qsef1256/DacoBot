package net.qsef1256.dacobot.core.ui.command.v2;

import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface DacoAbstractCommand {

    String getName();

    String getDesc();

    List<DacoAbstractCommand> getSubCommands();

    CommandData toCommandData();

    void runCommand(@NotNull SlashCommandEvent event);

}
