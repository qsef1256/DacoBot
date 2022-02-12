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
import net.qsef1256.dacobot.database.HibernateManager;
import net.qsef1256.dacobot.enums.DiaInfo;
import net.qsef1256.dacobot.util.GenericUtil;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Set;

import static org.reflections.scanners.Scanners.SubTypes;

public class DacoBot {

    public static final Logger logger = LoggerFactory.getLogger(DacoBot.class.getSimpleName());
    @Getter
    private static final Reflections reflections = new Reflections("net.qsef1256.dacobot");
    @Getter
    private static JDA jda;
    @Getter
    private static CommandClient commandClient;
    private static String[] args;

    public static void main(final String[] args) throws LoginException {
        if (args == null) {
            System.out.println("Please start bot with Discord Bot Token.");
            return;
        }
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
            logger.error("Error on loading commands");
            e.printStackTrace();
            System.exit(1);
        }

        commandClient = commandClientBuilder.build();
        logger.info(DiaInfo.BOT_NAME + " Prefix: '" + commandClient.getPrefix() + "'");

        final JDABuilder builder = JDABuilder.createDefault(args[0]);
        configureMemoryUsage(builder);
        builder.addEventListeners(commandClient);

        try {
            registerListeners(builder);
        } catch (final ReflectiveOperationException e) {
            logger.error("Error on loading listeners");
            e.printStackTrace();
            System.exit(1);
        }

        jda = builder.build();

        HelpCommand.initCommands();
        HibernateManager.getSessionFactoryFromJPA().openSession();
    }

    public static void configureMemoryUsage(final @NotNull JDABuilder builder) {
        builder.disableCache(CacheFlag.VOICE_STATE);
        builder.setChunkingFilter(ChunkingFilter.ALL); // 모든 길드의 유저 캐싱하기 (권한 필요)
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS); // 유저 캐싱 권한 얻기 (권한 필요)
        builder.disableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MESSAGE_TYPING);
    }

    private static void registerCommands(final CommandClientBuilder commandClientBuilder) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        logger.info("Loading Commands");
        final Set<Class<?>> commands = reflections.get(SubTypes.of(Command.class).asClass());

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

    private static void registerListeners(final JDABuilder builder) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        logger.info("Loading Listeners");
        final Set<Class<?>> listeners = reflections.get(SubTypes.of(ListenerAdapter.class).asClass());

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
        HibernateManager.shutdown();
    }

    // Warn: 새로 만든 봇은 추적되지 않음 (직접 닫아야 함)
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
