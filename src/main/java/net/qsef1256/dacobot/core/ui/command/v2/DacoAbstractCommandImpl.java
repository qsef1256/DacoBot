package net.qsef1256.dacobot.core.ui.command.v2;

import lombok.Getter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.qsef1256.dacobot.core.ui.command.v2.subcommand.SubCommandStrategy;

import java.util.List;

@Getter
public abstract class DacoAbstractCommandImpl implements DacoAbstractCommand {

    private String name;
    private String desc;
    private List<DacoAbstractCommandImpl> subCommands;

    private SubCommandStrategy subCommandStrategy;

    @Override
    public CommandData toCommandData() {
        return subCommandStrategy.toCommandData(name, desc, subCommands);
    }

}
