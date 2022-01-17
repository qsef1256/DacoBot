package net.qsef1256.diabot.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import static net.qsef1256.diabot.DiaBot.logger;

public class AdminCommand extends SlashCommand {

    public AdminCommand() {
        name = "diaadmin";
        help = "다이아 전용 관리용 명령어: !관계자외 접근 금지!";
        ownerCommand = true;
        defaultEnabled = false;

        children = new SlashCommand[]{
                new StopCommand()
        };

    }

    @Override
    public void execute(SlashCommandEvent event) {
        event.reply("추가 명령어를 입력하세요! : " + getHelp()).queue();
    }

    private static class StopCommand extends SlashCommand {
        public StopCommand() {
            this.name = "stop";
            this.help = "장비를 정지합니다.";
        }

        @Override
        public void execute(SlashCommandEvent event) {
            logger.info("Shutting down with command");
            event.reply("끄는 중....").setEphemeral(true).queue((msg) -> {
                if (event.getGuild() != null) {
                    event.getGuild().updateCommands().queue();
                }
                event.getJDA().shutdown();
            });
        }
    }

}
