package net.qsef1256.dacobot.core.ui.command.v2.subcommand;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import net.qsef1256.dacobot.core.ui.command.v2.DacoAbstractCommandImpl;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class StandardSubCommandStrategy implements SubCommandStrategy {

    @Override
    public CommandData toCommandData(@NotNull String name,
                                     @NotNull String desc,
                                     @NotNull List<DacoAbstractCommandImpl> subcommands) {
        return new CommandDataImpl(name, desc)
                .addSubcommands(subcommands.stream()
                        .map(subCommand -> new SubcommandData(subCommand.getName(), subCommand.getDesc()))
                        .toList());
    }

}
