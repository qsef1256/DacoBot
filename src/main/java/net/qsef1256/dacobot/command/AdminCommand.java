package net.qsef1256.dacobot.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.qsef1256.dacobot.DacoBot;
import net.qsef1256.dacobot.ui.DiaEmbed;
import net.qsef1256.dacobot.ui.DiaMessage;
import net.qsef1256.dacobot.util.JDAUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

import static net.qsef1256.dacobot.DacoBot.logger;

public class AdminCommand extends SlashCommand {

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
    public void execute(final @NotNull SlashCommandEvent event) {
        SlashCommand[] children = getChildren();

        event.reply(DiaMessage.needSubCommand(children, event.getMember())).queue();
    }

    private static class StopCommand extends SlashCommand {

        public StopCommand() {
            name = "자자";
            help = "장비를 정지합니다.";

            ownerCommand = true;
        }

        @Override
        public void execute(final @NotNull SlashCommandEvent event) {
            logger.info("Shutting down with command");
            event.reply("끄는 중....").setEphemeral(true).queue(msg ->
                    DacoBot.shutdown());
        }

    }

    private static class SayCommand extends SlashCommand {

        public SayCommand() {
            name = "말해";
            help = "메시지 보내기";
            ownerCommand = true;

            options = Collections.singletonList(new OptionData(OptionType.STRING, "메시지", "시킬 말").setRequired(true));
        }

        @Override
        public void execute(final @NotNull SlashCommandEvent event) {
            final OptionMapping option = JDAUtil.getOptionMapping(event, "메시지");
            if (option == null) return;

            event.deferReply().queue(m -> m.deleteOriginal().queue());
            event.getChannel().sendMessage(option.getAsString()).queue();
        }

    }

    private static class ClearCommand extends SlashCommand {

        public ClearCommand() {
            name = "초기화";
            help = "명령어를 초기화 하고 종료 합니다.";
            ownerCommand = true;
        }

        @Override
        protected void execute(SlashCommandEvent event) {
            JDA jda = DacoBot.getJda();
            String forcedGuildId = DacoBot.getCommandClient().forcedGuildId();

            try {
                jda.awaitReady();
            } catch (InterruptedException e) {
                event.replyEmbeds(DiaEmbed.error(null, null, e, null).build()).queue();
                e.printStackTrace();
                return;
            }

            final Guild guild = jda.getGuildById(forcedGuildId);
            if (guild != null) {
                logger.info("Cleaning Commands");
                guild.updateCommands().queue();
            } else {
                logger.warn("forced Guild is null");
            }

            event.reply("초기화가 완료 되었습니다. 길드 ID: " + forcedGuildId).setEphemeral(true).queue();
            DacoBot.shutdown();
        }

    }

    private static class RestartCommand extends SlashCommand {

        public RestartCommand() {
            name = "재시작";
            help = "다봇을 재시작 합니다.";
            ownerCommand = true;
        }

        @Override
        protected void execute(@NotNull SlashCommandEvent event) {
            event.reply("재시작 진행 중... 새 봇은 개발 툴에서 추적되지 않으니 주의하세요.").setEphemeral(true).queue();
            DacoBot.restart();
        }

    }

}
