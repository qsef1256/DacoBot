package net.qsef1256.dacobot.core.config;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.command.SlashCommand;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Activity;
import net.qsef1256.dacobot.command.tool.hangeul.EngKorContextMenu;
import net.qsef1256.dacobot.core.listener.CommandHandler;
import net.qsef1256.dacobot.setting.DiaSetting;
import net.qsef1256.dialib.util.GenericUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
public class CommandClientConfig {

    private final DiaSetting setting;
    private final List<? extends Command> commands;

    public CommandClientConfig(@NotNull DiaSetting setting,
                               @Qualifier("botCommands") List<? extends Command> commands) {
        this.setting = setting;
        this.commands = commands;
    }

    @Bean(name = "commandClient")
    public CommandClient getCommandClient() {
        CommandClientBuilder commandClientBuilder = new CommandClientBuilder();
        configureCommandClientBuilder(commandClientBuilder);
        registerCommands(commandClientBuilder);
        registerContextMenu(commandClientBuilder);

        CommandClient commandClient = commandClientBuilder.build();
        commandClient.setListener(new CommandHandler(commandClient));

        return commandClient;
    }

    private void configureCommandClientBuilder(@NotNull CommandClientBuilder commandClientBuilder) {
        commandClientBuilder.setOwnerId(setting.getSetting().getString("bot.ownerId"));
        commandClientBuilder.setActivity(Activity.playing("다코 가동 중..."));
        commandClientBuilder.forceGuildOnly(setting.getMainGuildID());
        commandClientBuilder.useHelpBuilder(true);
        commandClientBuilder.setHelpWord("도움말");
        commandClientBuilder.setHelpConsumer(event -> event.reply("/도움말을 입력해주세요."));
        commandClientBuilder.setPrefix("다코야");
        commandClientBuilder.setManualUpsert(true); // TODO: Endpoint disabled (https://discord.com/developers/docs/change-log#updated-command-permissions)
    }

    private void registerCommands(@NotNull CommandClientBuilder commandClientBuilder) {
        log.info("Loading Commands");

        if (commands.isEmpty())
            log.warn("There is no command in the registered package. No commands were loaded.");
        for (Command command : commands) {
            String commandType = "Unknown";
            if (GenericUtil.typeOf(command.getClass().getSuperclass(), Command.class)) {
                commandType = "Command";
                commandClientBuilder.addCommand(command);
            }
            if (GenericUtil.typeOf(command.getClass().getSuperclass(), SlashCommand.class)) {
                commandType = "Slash";
                commandClientBuilder.addSlashCommand((SlashCommand) command);
            }

            log.info("Loaded %s %s successfully".formatted(commandType, command.getClass().getSimpleName()));
        }
    }

    private void registerContextMenu(@NotNull CommandClientBuilder commandClientBuilder) {
        log.info("Loading Context Menus");

        commandClientBuilder.addContextMenu(new EngKorContextMenu());
    }

}
