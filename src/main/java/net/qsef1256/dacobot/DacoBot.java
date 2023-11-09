package net.qsef1256.dacobot;

import com.jagrosh.jdautilities.command.*;
import jakarta.persistence.EntityTransaction;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.qsef1256.dacobot.command.HelpCommand;
import net.qsef1256.dacobot.command.tool.hangeul.EngKorContextMenu;
import net.qsef1256.dacobot.core.schedule.DiaScheduler;
import net.qsef1256.dacobot.database.JpaController;
import net.qsef1256.dacobot.listener.CommandHandler;
import net.qsef1256.dacobot.setting.DiaSetting;
import net.qsef1256.dacobot.setting.constants.DiaInfo;
import net.qsef1256.dacobot.util.ReflectionUtil;
import net.qsef1256.dialib.util.GenericUtil;
import net.qsef1256.dialib.util.LocalDateTimeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootApplication
public class DacoBot implements CommandLineRunner {

    public static final Logger logger = LoggerFactory.getLogger(DacoBot.class);

    @Getter
    private static JDA jda;
    @Getter
    private static CommandClient commandClient;
    private final List<? extends Command> commands;
    private final List<? extends ListenerAdapter> listeners;

    private String[] args;

    @Autowired
    public DacoBot(List<? extends Command> commands,
                   List<? extends ListenerAdapter> listeners) {
        this.commands = commands;
        this.listeners = listeners;
    }

    public static void main(final String[] args) {
        SpringApplication.run(DacoBot.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        this.args = args;

        logger.info(DiaInfo.BOT_NAME + " is Starting!");
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));

        CommandClientBuilder commandClientBuilder = new CommandClientBuilder();
        configureCommandClientBuilder(commandClientBuilder);
        registerCommands(commandClientBuilder);
        registerContextMenu(commandClientBuilder);

        commandClient = commandClientBuilder.build();
        commandClient.setListener(new CommandHandler());
        logger.info("%s Prefix: '%s'".formatted(DiaInfo.BOT_NAME, commandClient.getPrefix()));

        initJpa();
        JDABuilder builder = JDABuilder.createDefault(DiaSetting.getInstance()
                .getKey()
                .getString("discord.token"));
        configureBot(builder);
        builder.addEventListeners(commandClient);
        registerListeners(builder);

        HelpCommand.initCommands();
        jda = builder.build();
        jda.awaitReady();

        // TODO: global command
        DiaSetting.getInstance()
                .getAllGuilds()
                .forEach(this::upsertToGuild);
        LocalDateTimeUtil.setZoneId(DiaSetting.getInstance().getZoneId());

        logger.info("Finish loading " + DiaInfo.BOT_NAME + "!");
    }

    private void initJpa() {
        EntityTransaction transaction = JpaController.getEntityManager().getTransaction();
        transaction.begin();
        transaction.commit();
    }

    private void configureCommandClientBuilder(@NotNull CommandClientBuilder commandClientBuilder) {
        commandClientBuilder.setOwnerId(DiaSetting.getInstance().getSetting().getString("bot.ownerId"));
        commandClientBuilder.setActivity(Activity.playing("다코 가동 중..."));
        commandClientBuilder.forceGuildOnly(DiaSetting.getInstance().getMainGuildID());
        commandClientBuilder.useHelpBuilder(true);
        commandClientBuilder.setHelpWord("도움말");
        commandClientBuilder.setHelpConsumer(event -> event.reply("/도움말을 입력해주세요."));
        commandClientBuilder.setPrefix("다코야");
        commandClientBuilder.setManualUpsert(true); // TODO: Endpoint disabled (https://discord.com/developers/docs/change-log#updated-command-permissions)
    }

    private void configureBot(@NotNull JDABuilder builder) {
        builder.disableCache(CacheFlag.VOICE_STATE);
        builder.setMemberCachePolicy(MemberCachePolicy.ALL); // 모든 길드의 유저 캐싱하기
        builder.setChunkingFilter(ChunkingFilter.ALL);
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT);
        builder.disableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MESSAGE_TYPING);
    }

    private void registerCommands(@NotNull CommandClientBuilder commandClientBuilder) {
        log.info("Loading Commands");

        if (commands.isEmpty())
            log.warn("There is no command in the registered package. No commands were loaded.");
        for (Command command : commands) {
            String typeDisplay = "Unknown";
            if (GenericUtil.typeOf(command.getClass().getSuperclass(), Command.class)) {
                typeDisplay = "Command";
                commandClientBuilder.addCommand(command);
            }
            if (GenericUtil.typeOf(command.getClass().getSuperclass(), SlashCommand.class)) {
                typeDisplay = "Slash";
                commandClientBuilder.addSlashCommand((SlashCommand) command);
            }

            log.info("Loaded %s %s successfully".formatted(typeDisplay, command.getClass().getSimpleName()));
        }
    }

    private void registerContextMenu(@NotNull CommandClientBuilder commandClientBuilder) {
        logger.info("Loading Context Menus");

        commandClientBuilder.addContextMenu(new EngKorContextMenu());
    }

    @SneakyThrows
    private void registerListeners(@NotNull JDABuilder builder) {
        logger.info("Loading Listeners");

        if (listeners.isEmpty())
            logger.warn("There is no listener in the registered package. No listeners were loaded.");
        for (ListenerAdapter listener : listeners) {
            if (!ReflectionUtil.isConcrete(listener.getClass())) continue;

            builder.addEventListeners(listener);
            logger.info("Loaded %s successfully".formatted(listener.getClass().getSimpleName()));
        }
    }

    private void upsertToGuild(@Nullable Guild guild) {
        if (guild != null) {
            logger.info("Upsert command data for Guild id %s".formatted(guild.getId()));

            List<CommandData> commandDataList = commandClient.getSlashCommands()
                    .stream()
                    .map(SlashCommand::buildCommandData)
                    .toList();
            List<CommandData> contextMenuList = commandClient.getContextMenus()
                    .stream()
                    .map(ContextMenu::buildCommandData)
                    .toList();

            List<CommandData> allCommandData = new ArrayList<>();
            allCommandData.addAll(commandDataList);
            allCommandData.addAll(contextMenuList);

            guild.updateCommands()
                    .addCommands(allCommandData)
                    .queue();
        } else {
            logger.warn("Cannot find main Guild");
        }
    }

    public void shutdown() {
        logger.info("Shutting down...");

        DiaScheduler.shutdown();
        jda.shutdown();
        JpaController.shutdown();
    }

    /**
     * <b>주의:</b> 새로 만든 봇은 추적되지 않음 (직접 닫아야 함)
     */
    public void restart() {
        StringBuilder cmd = new StringBuilder();
        cmd.append(System.getProperty("java.home"))
                .append(File.separator)
                .append("bin")
                .append(File.separator)
                .append("java ");
        for (String jvmArg : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
            cmd.append(jvmArg).append(" ");
        }
        cmd.append("-cp ")
                .append(ManagementFactory.getRuntimeMXBean().getClassPath())
                .append(" ")
                .append(DacoBot.class.getName())
                .append(" ");
        for (String arg : args) {
            cmd.append(arg).append(" ");
        }
        try {
            Runtime.getRuntime().exec(cmd.toString());
        } catch (IOException e) {
            logger.error("Failed to start bot: %s".formatted(e.getMessage()), e);

            return;
        }
        shutdown();
    }

}
