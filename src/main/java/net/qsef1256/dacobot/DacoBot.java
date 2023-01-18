package net.qsef1256.dacobot;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.command.SlashCommand;
import jakarta.persistence.EntityTransaction;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.qsef1256.dacobot.command.HelpCommand;
import net.qsef1256.dacobot.database.JpaController;
import net.qsef1256.dacobot.game.chat.listener.TalkListener;
import net.qsef1256.dacobot.schedule.DiaScheduler;
import net.qsef1256.dacobot.service.openapi.corona.CoronaApi;
import net.qsef1256.dacobot.setting.DiaSetting;
import net.qsef1256.dacobot.setting.constants.DiaInfo;
import net.qsef1256.dacobot.util.ReflectionUtil;
import net.qsef1256.dialib.util.GenericUtil;
import net.qsef1256.dialib.util.LocalDateTimeUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Set;

import static org.reflections.scanners.Scanners.SubTypes;

public class DacoBot {

    public static final Logger logger = LoggerFactory.getLogger(DacoBot.class.getSimpleName());
    @Getter
    private static JDA jda;
    @Getter
    private static CommandClient commandClient;
    private static String[] args;

    public static void main(final String[] args) throws LoginException, InterruptedException {
        String token = DiaSetting.getKey().getProperty("discord.token");

        DacoBot.args = args;

        logger.info(DiaInfo.BOT_NAME + " is Starting!");
        Runtime.getRuntime().addShutdownHook(new Thread(DacoBot::shutdown));

        final CommandClientBuilder commandClientBuilder = new CommandClientBuilder();
        commandClientBuilder.setOwnerId(DiaSetting.getSetting().getProperty("bot.ownerId"));
        commandClientBuilder.setActivity(Activity.playing("다코 가동 중..."));
        commandClientBuilder.forceGuildOnly(DiaSetting.getSetting().getProperty("bot.mainGuildId"));
        commandClientBuilder.useHelpBuilder(true);
        commandClientBuilder.setHelpWord("도움말");
        commandClientBuilder.setHelpConsumer(event -> event.reply("/도움말을 입력해주세요."));
        commandClientBuilder.setPrefix("다코야 ");
        commandClientBuilder.setManualUpsert(true); // TODO: Endpoint disabled (https://discord.com/developers/docs/change-log#updated-command-permissions)

        try {
            registerCommands(commandClientBuilder);
        } catch (final ReflectiveOperationException e) {
            exit("Error on loading commands", e);
        }

        commandClient = commandClientBuilder.build();
        commandClient.setListener(new TalkListener());
        logger.info("%s Prefix: '%s'".formatted(DiaInfo.BOT_NAME, commandClient.getPrefix()));

        initJpa();

        final JDABuilder builder = JDABuilder.createDefault(token);
        configureMemoryUsage(builder);
        builder.addEventListeners(commandClient);

        try {
            registerListeners(builder);
        } catch (final ReflectiveOperationException e) {
            exit("Error on loading listeners", e);
        }

        HelpCommand.initCommands();
        jda = builder.build();
        jda.awaitReady();

        // TODO: global command
        DiaSetting.getAllGuilds().forEach(DacoBot::upsertGuildCommands);

        LocalDateTimeUtil.setZoneId(DiaSetting.getZoneId());
        DiaScheduler.executePerTime(() -> new CoronaApi().update(), 12, 0, 0);

        logger.info("Finish loading " + DiaInfo.BOT_NAME + "!");
    }

    private static void initJpa() {
        EntityTransaction transaction = JpaController.getEntityManager().getTransaction();
        transaction.begin();
        transaction.commit();
    }

    private static void configureMemoryUsage(final @NotNull JDABuilder builder) {
        builder.disableCache(CacheFlag.VOICE_STATE);
        builder.setChunkingFilter(ChunkingFilter.ALL); // 모든 길드의 유저 캐싱하기 (권한 필요)
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS); // 유저 캐싱 권한 얻기 (권한 필요)
        builder.disableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MESSAGE_TYPING);
    }

    private static void registerCommands(final CommandClientBuilder commandClientBuilder) throws ReflectiveOperationException {
        logger.info("Loading Commands");
        final Set<Class<?>> commands = ReflectionUtil.getReflections().get(SubTypes.of(Command.class).asClass());

        if (commands.isEmpty())
            logger.warn("There is no command in the registered package. No commands were loaded.");
        for (final Class<?> command : commands) {
            if (!ReflectionUtil.isPlain(command)) continue;

            String typeDisplay = "Unknown";
            if (GenericUtil.typeOf(command.getSuperclass(), Command.class)) {
                typeDisplay = "Command";
                Command instance = (Command) command.getConstructor().newInstance();
                commandClientBuilder.addCommand(instance);
            }
            if (GenericUtil.typeOf(command.getSuperclass(), SlashCommand.class)) {
                typeDisplay = "Slash";
                SlashCommand instance = (SlashCommand) command.getConstructor().newInstance();
                commandClientBuilder.addSlashCommand(instance);
            }

            logger.info("Loaded %s %s successfully".formatted(typeDisplay, command.getSimpleName()));
        }
    }

    private static void registerListeners(final JDABuilder builder) throws ReflectiveOperationException {
        logger.info("Loading Listeners");
        final Set<Class<?>> listeners = ReflectionUtil.getReflections().get(SubTypes.of(ListenerAdapter.class).asClass());

        if (listeners.isEmpty())
            logger.warn("There is no listener in the registered package. No listeners were loaded.");
        for (final Class<?> listener : listeners) {
            if (!ReflectionUtil.isConcrete(listener)) continue;

            ListenerAdapter slashCommand = (ListenerAdapter) listener.getConstructor().newInstance();
            builder.addEventListeners(slashCommand);
            logger.info("Loaded %s successfully".formatted(listener.getSimpleName()));
        }
    }

    private static void upsertGuildCommands(Guild mainGuild) {
        if (mainGuild != null) {
            String formatted = "Upsert commands for Guild id %s".formatted(mainGuild.getId());

            logger.info(formatted);
            List<CommandData> commandDataList = commandClient.getSlashCommands()
                    .stream()
                    .map(SlashCommand::buildCommandData)
                    .toList();
            mainGuild.updateCommands().addCommands(commandDataList).queue();
        } else {
            logger.warn("Cannot find main Guild");
        }
    }

    public static void shutdown() {
        DiaScheduler.shutdown();
        jda.shutdown();
        JpaController.shutdown();
    }

    private static void exit(String message, @NotNull Exception e) {
        logger.error(message);
        e.printStackTrace();
        System.exit(1);
    }

    /**
     * <b>주의:</b> 새로 만든 봇은 추적되지 않음 (직접 닫아야 함)
     */
    public static void restart() {
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
        for (String arg : DacoBot.args) {
            cmd.append(arg).append(" ");
        }
        try {
            Runtime.getRuntime().exec(cmd.toString());
        } catch (IOException e) {
            logger.error("Failed to start bot: %s".formatted(e.getMessage()));
            e.printStackTrace();
            return;
        }
        shutdown();
    }

}
