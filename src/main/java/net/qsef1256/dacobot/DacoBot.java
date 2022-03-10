package net.qsef1256.dacobot;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.command.SlashCommand;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.qsef1256.dacobot.command.HelpCommand;
import net.qsef1256.dacobot.database.JPAManager;
import net.qsef1256.dacobot.enums.DiaInfo;
import net.qsef1256.dacobot.setting.DiaSetting;
import net.qsef1256.dacobot.util.GenericUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Modifier;
import java.util.Set;

import static org.reflections.scanners.Scanners.SubTypes;

public class DacoBot {

    public static final Logger logger = LoggerFactory.getLogger(DacoBot.class.getSimpleName());
    @Getter
    private static JDA jda;
    @Getter
    private static CommandClient commandClient;
    private static String[] args;

    public static void main(final String[] args) throws LoginException {
        String token = DiaSetting.getKey().getProperty("discord.token");

        DacoBot.args = args;

        logger.info(DiaInfo.BOT_NAME + " is Starting!");
        final CommandClientBuilder commandClientBuilder = new CommandClientBuilder();
        commandClientBuilder.setOwnerId("419761037861060619");
        commandClientBuilder.forceGuildOnly("889451044445224970");
        commandClientBuilder.setActivity(Activity.playing("다코 가동 중..."));
        commandClientBuilder.useHelpBuilder(true);
        commandClientBuilder.setHelpWord("도움말");
        commandClientBuilder.setHelpConsumer((event) -> event.reply("/도움말을 입력해주세요."));
        commandClientBuilder.setPrefix("다코야 ");

        try {
            registerCommands(commandClientBuilder);
        } catch (final ReflectiveOperationException e) {
            exit("Error on loading commands", e);
        }

        commandClient = commandClientBuilder.build();
        logger.info(DiaInfo.BOT_NAME + " Prefix: '" + commandClient.getPrefix() + "'");

        final JDABuilder builder = JDABuilder.createDefault(token);
        configureMemoryUsage(builder);
        builder.addEventListeners(commandClient);

        try {
            registerListeners(builder);
        } catch (final ReflectiveOperationException e) {
            exit("Error on loading listeners", e);
        }

        jda = builder.build();

        HelpCommand.initCommands();
        JPAManager.getSessionFactoryFromJPA().openSession();
    }

    private static void exit(String message, @NotNull Exception e) {
        logger.error(message);
        e.printStackTrace();
        System.exit(1);
    }

    private static void configureMemoryUsage(final @NotNull JDABuilder builder) {
        builder.disableCache(CacheFlag.VOICE_STATE);
        builder.setChunkingFilter(ChunkingFilter.ALL); // 모든 길드의 유저 캐싱하기 (권한 필요)
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS); // 유저 캐싱 권한 얻기 (권한 필요)
        builder.disableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MESSAGE_TYPING);
    }

    private static void registerCommands(final CommandClientBuilder commandClientBuilder) throws ReflectiveOperationException {
        logger.info("Loading Commands");
        final Set<Class<?>> commands = DiaSetting.getReflections().get(SubTypes.of(Command.class).asClass());

        if (commands.size() == 0)
            logger.warn("There is no command in the registered package. No commands were loaded.");
        for (final Class<?> command : commands) {
            if (command.isMemberClass()) continue;
            if (command.isInterface()) continue;
            if (Modifier.isAbstract(command.getModifiers())) continue;

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
        final Set<Class<?>> listeners = DiaSetting.getReflections().get(SubTypes.of(ListenerAdapter.class).asClass());

        if (listeners.size() == 0)
            logger.warn("There is no listener in the registered package. No listeners were loaded.");
        for (final Class<?> listener : listeners) {
            ListenerAdapter slashCommand = (ListenerAdapter) listener.getConstructor().newInstance();
            builder.addEventListeners(slashCommand);
            logger.info("Loaded " + listener.getSimpleName() + " successfully");
        }
    }

    public static void shutdown() {
        jda.shutdown();
        JPAManager.shutdown();
    }

    // 주의: 새로 만든 봇은 추적되지 않음 (직접 닫아야 함)
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
            logger.error("Failed to start bot: " + e.getMessage());
            e.printStackTrace();
        }
        shutdown();
    }

}
