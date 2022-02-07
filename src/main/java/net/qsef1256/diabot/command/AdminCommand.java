package net.qsef1256.diabot.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.qsef1256.diabot.database.HibernateManager;

import java.util.Collections;

import static net.qsef1256.diabot.DiaBot.logger;

public class AdminCommand extends SlashCommand {

    public AdminCommand() {
        name = "다야야";
        help = "다이아 전용 관리용 명령어, 다이아만 접근 가능..?";
        ownerCommand = true;
        defaultEnabled = false;

        children = new SlashCommand[]{
                new StopCommand(),
                new SayCommand()
        };
    }

    @Override
    public void execute(final SlashCommandEvent event) {
        event.reply("추가 명령어를 입력하세요! : " + getHelp()).queue();
    }

    private static class StopCommand extends SlashCommand {
        public StopCommand() {
            name = "자자";
            help = "장비를 정지합니다.";
        }

        @Override
        public void execute(final SlashCommandEvent event) {
            logger.info("Shutting down with command");
            event.reply("끄는 중....").setEphemeral(true).queue((msg) -> {
                event.getJDA().shutdown();
                HibernateManager.shutdown();
            });
        }
    }

    private static class SayCommand extends SlashCommand {
        public SayCommand() {
            name = "말해";
            help = "메시지 보내기";
            options = Collections.singletonList(new OptionData(OptionType.STRING, "메시지", "시킬 말").setRequired(true));
        }

        @Override
        public void execute(final SlashCommandEvent event) {
            final OptionMapping option = event.getOption("메시지");
            if (option == null) {
                event.reply("메시지를 입력해주세요.").setEphemeral(true).queue();
                return;
            }
            event.deferReply().queue(m -> m.deleteOriginal().queue());
            event.getChannel().sendMessage(option.getAsString()).queue();
        }
    }

}
