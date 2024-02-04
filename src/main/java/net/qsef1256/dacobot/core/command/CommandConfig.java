package net.qsef1256.dacobot.core.command;

import com.jagrosh.jdautilities.command.Command;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
public class CommandConfig {

    @Bean(name = "botCommands")
    public List<Command> getCommands(@NotNull Map<String, ? extends Command> beans) {
        return beans.values().stream()
                .filter(bean -> !bean.getClass().isMemberClass())
                .map(Command.class::cast)
                .toList();
    }

    @Bean(name = "botSubCommands")
    public List<Command> getSubCommands(@NotNull Map<String, ? extends Command> beans) {
        return beans.values().stream()
                .filter(bean -> bean.getClass().isMemberClass())
                .map(Command.class::cast)
                .toList();
    }

}
