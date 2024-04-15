package net.qsef1256.dacobot.core.ui.command.v2.subcommand;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.qsef1256.dacobot.core.ui.command.v2.DacoAbstractCommandImpl;
import org.jetbrains.annotations.NotNull;

import java.util.List;

// TODO: change to state pattern
public interface SubCommandStrategy {

    CommandData toCommandData(@NotNull String name,
                              @NotNull String desc,
                              @NotNull List<DacoAbstractCommandImpl> subCommands);

}
