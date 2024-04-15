package net.qsef1256.dacobot.core.ui.command.v2.subcommand;

import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import net.qsef1256.dacobot.core.ui.command.v2.DacoAbstractCommandImpl;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OptionSubCommandStrategy implements SubCommandStrategy {

    @Override
    public CommandData toCommandData(@NotNull String name,
                                     @NotNull String desc,
                                     @NotNull List<DacoAbstractCommandImpl> subCommands) {
        OptionData option = new OptionData(OptionType.STRING, "종류", "명령어 종류")
                .addChoices(subCommands.stream()
                        .map(subCommand -> new Command.Choice(subCommand.getName(), subCommand.getName()))
                        .toList());

        return new CommandDataImpl(name, desc)
                .addOptions(option);
    }

}
