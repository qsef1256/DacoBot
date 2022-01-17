package net.qsef1256.diabot.game.explosion.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.sun.jdi.request.DuplicateRequestException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.Button;
import net.qsef1256.diabot.enums.DiaColor;
import net.qsef1256.diabot.model.DiscordManager;

import java.sql.SQLException;

public class ExplosionCommand extends SlashCommand {

    public ExplosionCommand() {
        name = "폭발";
        help = "폭발은 예술이다... 폭발 게임을 시작합니다";

        children = new SlashCommand[]{
                new RegisterCommand(),
                new ResetCommand()
        };
    }

    @Override
    protected void execute(final SlashCommandEvent event) {
        event.reply("추가 명령어를 입력하세요! : " + getHelp()).queue();
    }

    private static class RegisterCommand extends SlashCommand {

        public RegisterCommand() {
            name = "등록";
            help = "폭발물 취급 허가를 얻습니다.";
        }

        @Override
        protected void execute(final SlashCommandEvent event) {
            final Member member = event.getMember();
            if (member == null) return;

            event.deferReply().queue(e -> {
                try {
                    new DiscordManager().register(member.getIdLong());
                    e.editOriginalEmbeds(new EmbedBuilder()
                            .setTitle("등록 성공")
                            .setColor(DiaColor.SUCCESS)
                            .setAuthor(member.getUser().getAsTag(), null, member.getAvatarUrl())
                            .setDescription("정상적으로 등록 되었습니다.")
                            .addField("", "시작하기 전에, /규칙 명령어를 확인해주세요.", false)
                            .setFooter("/폭발 커맨드로 게임을 시작하세요!")
                            .build()
                    ).queue();
                } catch (SQLException | RuntimeException ex) {
                    e.editOriginalEmbeds(new EmbedBuilder()
                            .setTitle("등록 실패")
                            .setColor(DiaColor.FAIL)
                            .setAuthor(member.getUser().getAsTag(), null, member.getAvatarUrl())
                            .setDescription(ex.getMessage())
                            .build()
                    ).queue();
                    
                    if (ex instanceof DuplicateRequestException) return;
                    ex.printStackTrace();
                }
            });
        }

    }

    private static class ResetCommand extends SlashCommand {

        public ResetCommand() {
            name = "초기화";
            help = "당신의 계정을 폭파시킵니다. 주의! 되돌릴 수 없습니다.";
        }

        @Override
        protected void execute(final SlashCommandEvent event) {
            event.replyEmbeds(new EmbedBuilder()
                            .setTitle("계정 초기화 확인")
                            .setColor(DiaColor.SEVERE)
                            .setDescription("당신의 계정을 초기화 합니다.\n초기화를 원하시면 아래의 버튼을 눌러주세요.")
                            .addField("주의!", "**되돌릴 수 없습니다.**", false)
                            .setFooter("이렇게까지 만들었는데도 날려먹으면 네 탓!!!")
                            .build())
                    .addActionRow(
                            Button.danger("explosion_reset", "초기화"))
                    .setEphemeral(true)
                    .queue();
        }

    }
}
