package net.qsef1256.dacobot.core.boot;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.qsef1256.dacobot.DacoBot;
import net.qsef1256.dacobot.core.command.CommandClientService;
import net.qsef1256.dacobot.core.jda.JdaService;
import net.qsef1256.dacobot.core.schedule.DiaScheduler;
import net.qsef1256.dacobot.setting.DiaSetting;
import net.qsef1256.dacobot.setting.constants.DiaInfo;
import net.qsef1256.dialib.util.LocalDateTimeUtil;
import org.jetbrains.annotations.NotNull;
import org.mariuszgromada.math.mxparser.License;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.Objects;

// TODO: separate shutdown, restart to Configuration?
@Slf4j
@Component
public class DacoBootstrapper {

    private final ApplicationArguments args;
    private final JdaService jda;
    private final CommandClientService commandClient;
    private final DiaSetting setting;
    private final DiaScheduler scheduler;

    public DacoBootstrapper(@NotNull ApplicationArguments args,
                            @NotNull DiaSetting setting,
                            @NotNull DiaScheduler scheduler,
                            @NotNull JdaService jda,
                            @NotNull CommandClientService commandClient) {
        this.args = args;
        this.setting = setting;
        this.scheduler = scheduler;
        this.jda = jda;
        this.commandClient = commandClient;
    }

    @PostConstruct
    public void boot() throws Exception {
        log.info(DiaInfo.BOT_NAME + " is Starting!");
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
        guilds.forEach(commandClient::upsertToGuild);

        License.iConfirmNonCommercialUse("qsef1256");
        LocalDateTimeUtil.setZoneId(setting.getZoneId());

        log.info("Finish loading " + DiaInfo.BOT_NAME + "!");
    }

    @PreDestroy
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
        for (String arg : args.getNonOptionArgs())
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
