package net.qsef1256.dacobot;

import com.jagrosh.jdautilities.command.ContextMenu;
import com.jagrosh.jdautilities.command.SlashCommand;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.qsef1256.dacobot.core.command.CommandClientService;
import net.qsef1256.dacobot.core.jda.JdaService;
import net.qsef1256.dacobot.core.schedule.DiaScheduler;
import net.qsef1256.dacobot.setting.DiaSetting;
import net.qsef1256.dacobot.setting.constants.DiaInfo;
import net.qsef1256.dialib.util.LocalDateTimeUtil;
import org.jetbrains.annotations.NotNull;
import org.mariuszgromada.math.mxparser.License;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@SpringBootApplication
public class DacoBot implements CommandLineRunner {

    private final JdaService jda;
    private final CommandClientService commandClient;
    private final DiaSetting setting;
    private final DiaScheduler scheduler;

    private String[] args;

    public DacoBot(@NotNull DiaSetting setting,
                   @NotNull DiaScheduler scheduler,
                   @NotNull JdaService jda,
                   @NotNull CommandClientService commandClient) {
        this.setting = setting;
        this.scheduler = scheduler;
        this.jda = jda;
        this.commandClient = commandClient;
    }

    public static void main(String[] args) {
        SpringApplication.run(DacoBot.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        this.args = args;

        log.info(DiaInfo.BOT_NAME + " is Starting!");
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
        log.info("%s Prefix: '%s'".formatted(
                DiaInfo.BOT_NAME,
                commandClient.getCommandClient().getPrefix()));

        jda.getJda().awaitReady();

        List<Guild> guilds = jda.getAllGuilds()
                .stream()
                .filter(Objects::nonNull)
                .toList();
        log.info("guilds size: " + guilds.size());
        // TODO: global command
        guilds.forEach(this::upsertToGuild);

        License.iConfirmNonCommercialUse("qsef1256");
        LocalDateTimeUtil.setZoneId(setting.getZoneId());

        log.info("Finish loading " + DiaInfo.BOT_NAME + "!");
    }

    private void upsertToGuild(@NotNull Guild guild) {
        log.info("Upsert command data for Guild id %s".formatted(guild.getId()));

        List<CommandData> commandDataList = commandClient.getCommandClient().getSlashCommands()
                .stream()
                .map(SlashCommand::buildCommandData)
                .toList();
        List<CommandData> contextMenuList = commandClient.getCommandClient().getContextMenus()
                .stream()
                .map(ContextMenu::buildCommandData)
                .toList();

        List<CommandData> allCommandData = new ArrayList<>();
        allCommandData.addAll(commandDataList);
        allCommandData.addAll(contextMenuList);

        guild.updateCommands()
                .addCommands(allCommandData)
                .queue();
    }

    public void shutdown() {
        log.info("Shutting down...");

        scheduler.shutdown();
        jda.getJda().shutdown();
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
        for (String jvmArg : ManagementFactory.getRuntimeMXBean().getInputArguments())
            cmd.append(jvmArg).append(" ");
        cmd.append("-cp ")
                .append(ManagementFactory.getRuntimeMXBean().getClassPath())
                .append(" ")
                .append(DacoBot.class.getName())
                .append(" ");
        for (String arg : args)
            cmd.append(arg).append(" ");

        try {
            Runtime.getRuntime().exec(cmd.toString());
        } catch (IOException e) {
            log.error("Failed to start bot: %s".formatted(e.getMessage()), e);

            return;
        }
        shutdown();
    }

}
