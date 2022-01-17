package net.qsef1256.diabot;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import com.jagrosh.jdautilities.command.SlashCommand;
import lombok.Getter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.qsef1256.diabot.database.MainDAO;
import net.qsef1256.diabot.database.MainDAOImpl;
import net.qsef1256.diabot.game.explosion.listener.ButtonListener;
import net.qsef1256.diabot.listener.MessageHandler;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import static org.reflections.scanners.Scanners.SubTypes;

public class DiaBot {

    public static Logger logger = LoggerFactory.getLogger(DiaBot.class.getSimpleName());
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
        commandClientBuilder.useHelpBuilder(false);
        commandClientBuilder.setPrefix("다야 ");

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
                commandClient,
                new MessageHandler(),
                new ButtonListener());

        jda = builder.build();
        final Guild guild = jda.getGuildById(commandClient.forcedGuildId());
        if (guild != null) {
            guild.updateCommands().queue();
        }

        final MainDAO dao = new MainDAOImpl();
        dao.init();

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

            commandClientBuilder.addSlashCommand((SlashCommand) command.getConstructor().newInstance());
            logger.info("Loaded " + command.getSimpleName() + " successfully");
        }
    }

}
