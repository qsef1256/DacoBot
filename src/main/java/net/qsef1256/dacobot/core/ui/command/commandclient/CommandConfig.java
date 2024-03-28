package net.qsef1256.dacobot.core.ui.command.commandclient;

import com.jagrosh.jdautilities.command.Command;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
public class CommandConfig {

    @Bean(name = "botCommands")
    public List<Command> getCommands(@NotNull List<? extends Command> commands) {
        return commands.stream()
                .filter(bean -> !bean.getClass().isMemberClass())
                .map(Command.class::cast)
                .toList();
    }

    @Bean(name = "botSubCommands")
    public List<Command> getSubCommands(@NotNull List<? extends Command> commands) {
        return commands.stream()
                .filter(bean -> bean.getClass().isMemberClass())
                .map(Command.class::cast)
                .toList();
    }

}
