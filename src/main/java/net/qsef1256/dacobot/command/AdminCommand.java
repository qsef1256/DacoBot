package net.qsef1256.dacobot.command;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.qsef1256.dacobot.core.boot.DacoBootstrapper;
import net.qsef1256.dacobot.core.ui.command.DacoCommand;
import net.qsef1256.dacobot.ui.DiaEmbed;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class AdminCommand extends DacoCommand {

    public AdminCommand() {
        name = "다코야";
        help = "다이아 전용 관리용 명령어, 다이아만 접근 가능..?";
        ownerCommand = true;

        children = new SlashCommand[]{
                new StopCommand(),
                new SayCommand(),
                new ClearCommand(),
                new RestartCommand()
        };
    }

    @Override
    public void runCommand(@NotNull SlashCommandEvent event) {
        callNeedSubCommand();
    }

    private static class StopCommand extends DacoCommand {

        @Setter(onMethod_ = {@Autowired})
        private DacoBootstrapper dacoBot;

        public StopCommand() {
            name = "자자";
            help = "장비를 정지합니다.";

            ownerCommand = true;
        }

        @Override
        public void runCommand(@NotNull SlashCommandEvent event) {
            log.info("Shutting down with command");

            event.reply("끄는 중....")
                    .setEphemeral(true)
                    .queue(msg -> dacoBot.shutdown());
        }

    }

    private static class SayCommand extends DacoCommand {

        public SayCommand() {
            name = "말해";
            help = "메시지 보내기";
            ownerCommand = true;

            options = List.of(new OptionData(OptionType.STRING, "메시지", "시킬 말").setRequired(true));
        }

        @Override
        public void runCommand(@NotNull SlashCommandEvent event) {
            String option = getOptionString("메시지");
            if (option == null) return;

            event.deferReply().queue(m -> m.deleteOriginal().queue());
            event.getChannel().sendMessage(option).queue();
        }

    }

    private static class ClearCommand extends DacoCommand {

        @Setter(onMethod_ = {@Autowired})
        private DacoBootstrapper dacoBot;
        @Setter(onMethod_ = {@Autowired})
        private JDA jda;
        @Setter(onMethod_ = {@Autowired})
        private CommandClient commandClient;

        public ClearCommand() {
            name = "초기화";
            help = "명령어를 초기화 하고 종료 합니다.";
            ownerCommand = true;
        }

        @Override
        @SneakyThrows
        protected void runCommand(@NotNull SlashCommandEvent event) {
            String forcedGuildId = commandClient.forcedGuildId();

            try {
                jda.awaitReady();
            } catch (InterruptedException e) {
                log.error("failed to clean command and restart", e);
                event.replyEmbeds(DiaEmbed.error(null, null, e, null).build()).queue();

                return;
            }

            final Guild guild = jda.getGuildById(forcedGuildId);
            if (guild != null) {
                log.info("Cleaning Commands");
                guild.updateCommands().queue();
            } else {
                log.warn("forced Guild is null");
            }

            event.reply("초기화가 완료 되었습니다. 길드 ID: " + forcedGuildId)
                    .setEphemeral(true)
                    .queue();

            dacoBot.shutdown();
        }

    }

    private static class RestartCommand extends DacoCommand {

        @Setter(onMethod_ = {@Autowired})
        private DacoBootstrapper dacoBot;

        public RestartCommand() {
            name = "재시작";
            help = "다봇을 재시작 합니다.";
            ownerCommand = true;
        }

        @Override
        protected void runCommand(@NotNull SlashCommandEvent event) {
            event.reply("재시작 진행 중... 새 봇은 개발 툴에서 추적되지 않으니 주의하세요.")
                    .setEphemeral(true)
                    .queue();

            dacoBot.restart();
        }

    }

}
