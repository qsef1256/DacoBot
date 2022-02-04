package net.qsef1256.diabot;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.command.SlashCommand;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.qsef1256.diabot.command.HelpCommand;
import net.qsef1256.diabot.model.HibernateManager;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import static org.reflections.scanners.Scanners.SubTypes;

public class DiaBot {

    public static final Logger logger = LoggerFactory.getLogger(DiaBot.class.getSimpleName());
    @Getter
    private static JDA jda;
    @Getter
    private static CommandClient commandClient;

    public static void main(final String[] args) throws LoginException {
        if (args == null) {
            System.out.println("Please start bot with Discord Bot Token.");
            return;
        }

        logger.info("DiaBot is Starting!");
        final CommandClientBuilder commandClientBuilder = new CommandClientBuilder();
        commandClientBuilder.setOwnerId("419761037861060619");
        commandClientBuilder.forceGuildOnly("889451044445224970");
        commandClientBuilder.setActivity(Activity.playing("다야 가동 중..."));
        commandClientBuilder.useHelpBuilder(true);
        commandClientBuilder.setHelpWord("도움말");
        commandClientBuilder.setHelpConsumer((event) -> event.reply("/도움말을 입력해주세요."));
        commandClientBuilder.setPrefix("다야야 ");

        try {
            registerCommands(commandClientBuilder);
        } catch (final ReflectiveOperationException e) {
            logger.error("Error on loading commands");
            e.printStackTrace();
            System.exit(1);
        }

        commandClient = commandClientBuilder.build();
        logger.info("DiaBot Prefix: '" + commandClient.getPrefix() + "'");

        final JDABuilder builder = JDABuilder.createDefault(args[0]);
        configureMemoryUsage(builder);
        builder.addEventListeners(
                commandClient);

        try {
            registerListeners(builder);
        } catch (final ReflectiveOperationException e) {
            logger.error("Error on loading commands");
            e.printStackTrace();
            System.exit(1);
        }

        jda = builder.build();
        final Guild guild = jda.getGuildById(commandClient.forcedGuildId());
        if (guild != null) {
            guild.updateCommands().queue();
        }

        HelpCommand.initCommands();
        HibernateManager.getSessionFactoryFromJPA().openSession();
    }

    public static void configureMemoryUsage(final JDABuilder builder) {
        builder.disableCache(CacheFlag.VOICE_STATE);
        builder.setChunkingFilter(ChunkingFilter.ALL); // 모든 길드의 유저 캐싱하기 (권한 필요)
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS); // 유저 캐싱 권한 얻기 (권한 필요)
        builder.disableIntents(GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_MESSAGE_TYPING);
    }

    private static void registerCommands(final CommandClientBuilder commandClientBuilder) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        logger.info("Loading Commands");
        final Reflections classes = new Reflections("net.qsef1256.diabot");
        final Set<Class<?>> commands = classes.get(SubTypes.of(SlashCommand.class).asClass());

        if (commands.size() == 0)
            logger.warn("There is no command in the registered package. No commands were loaded.");
        for (final Class<?> command : commands) {
            if (command.isMemberClass()) continue;

            SlashCommand slashCommand = (SlashCommand) command.getConstructor().newInstance();
            commandClientBuilder.addSlashCommand(slashCommand);
            logger.info("Loaded " + command.getSimpleName() + " successfully");
        }
    }

    private static void registerListeners(final JDABuilder builder) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        logger.info("Loading Listeners");
        final Reflections classes = new Reflections("net.qsef1256.diabot");
        final Set<Class<?>> listeners = classes.get(SubTypes.of(ListenerAdapter.class).asClass());

        if (listeners.size() == 0)
            logger.warn("There is no command in the registered package. No commands were loaded.");
        for (final Class<?> listener : listeners) {
            if (listener.isMemberClass()) continue;

            ListenerAdapter slashCommand = (ListenerAdapter) listener.getConstructor().newInstance();
            builder.addEventListeners(slashCommand);
            logger.info("Loaded " + listener.getSimpleName() + " successfully");
        }
    }

}
