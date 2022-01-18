package net.qsef1256.diabot.game.explosion.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.sun.jdi.request.DuplicateRequestException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.Button;
import net.qsef1256.diabot.data.DiscordUserData;
import net.qsef1256.diabot.enums.DiaColor;
import net.qsef1256.diabot.enums.DiaImage;
import net.qsef1256.diabot.enums.DiaInfo;
import net.qsef1256.diabot.game.explosion.data.DiscordCashData;
import net.qsef1256.diabot.game.explosion.model.ExplosionGameCore;
import net.qsef1256.diabot.game.explosion.model.ExplosionUser;
import net.qsef1256.diabot.model.DiscordManager;
import net.qsef1256.diabot.model.DiscordUser;

import java.sql.SQLException;
import java.util.NoSuchElementException;

public class ExplosionCommand extends SlashCommand {

    public ExplosionCommand() {
        name = "폭발";
        help = "폭발은 예술이다... 폭발 게임을 시작합니다";

        children = new SlashCommand[]{
                new RegisterCommand(),
                new StatusCommand(),
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
            final User user = event.getUser();

            event.deferReply().queue(e -> {
                try {
                    new DiscordManager().register(user.getIdLong());
                    ExplosionGameCore.register(user.getIdLong());
                    e.editOriginalEmbeds(new EmbedBuilder()
                            .setTitle("등록 성공")
                            .setColor(DiaColor.SUCCESS)
                            .setAuthor(user.getAsTag(), null, user.getAvatarUrl())
                            .setDescription("정상적으로 등록 되었습니다.")
                            .addField("", "시작하기 전에, /규칙 명령어를 확인해주세요.", false)
                            .setFooter("/폭발 커맨드로 게임을 시작하세요!")
                            .build()
                    ).queue();
                } catch (SQLException | RuntimeException ex) {
                    e.editOriginalEmbeds(new EmbedBuilder()
                            .setTitle("등록 실패")
                            .setColor(DiaColor.FAIL)
                            .setAuthor(user.getAsTag(), null, user.getAvatarUrl())
                            .setDescription(ex.getMessage())
                            .build()
                    ).queue();

                    if (ex instanceof DuplicateRequestException) return;
                    ex.printStackTrace();
                }
            });
        }

    }

    private static class StatusCommand extends SlashCommand {
        public StatusCommand() {
            name = "확인";
            help = "계정 상태를 확인합니다. 아니면 돈 자랑...";
        }

        @Override
        protected void execute(final SlashCommandEvent event) {
            final User user = event.getUser();

            try {
                final DiscordUserData userData = new DiscordUser(user.getIdLong()).getData();
                final DiscordCashData cashData = new ExplosionUser(user.getIdLong()).getData();
                final long cash = cashData.getCash();

                String footer = "아직 돈이 없군요. 돈을 벌어보세요!";
                footer = getFooter(cash, footer);

                event.replyEmbeds(new EmbedBuilder()
                                .setTitle("계정 정보")
                                .setColor(DiaColor.INFO)
                                .setAuthor(DiaInfo.BOT_NAME, null, DiaImage.MAIN_THUMBNAIL)
                                .setThumbnail(user.getAvatarUrl())
                                .addField("닉네임", user.getAsTag(), true)
                                .addField("가입 일자", userData.getRegister_time().toString(), true)
                                .addField("계정 상태", userData.getStatus(), true)
                                .addField("돈", cash + " 코인", false)
                                .setFooter(footer)
                                .build())
                        .queue();

            } catch (SQLException | RuntimeException e) {
                event.replyEmbeds(new EmbedBuilder()
                        .setTitle("문제가 생겼어요.")
                        .setColor(DiaColor.FAIL)
                        .setAuthor(user.getAsTag(), null, user.getAvatarUrl())
                        .setDescription(e.getMessage())
                        .setFooter("계정 신청은 /폭발 등록")
                        .build()
                ).queue();

                if (e instanceof NoSuchElementException) return;
                e.printStackTrace();
            }

        }

        private String getFooter(final long cash, String footer) {
            if (cash > 1) footer = "한푼 두푼 모으다 보면 많아질꺼에요.";
            if (cash > 100) footer = "멋지네요! 100 코인 돌파!";
            if (cash > 500) footer = "500 코인 돌파!";
            if (cash > 5000) footer = "이제 5천원 짜리 계정이네요. 잠깐.. 현실 돈이던가";
            if (cash > 10000) footer = "1만을 찍었어요. 더 벌어보자구요.";
            if (cash > 50000) footer = "돈 많아요!";
            return footer;
        }

    }

    private static class ResetCommand extends SlashCommand {

        public ResetCommand() {
            name = "초기화";
            help = "당신의 계정을 폭파시킵니다. 주의! 되돌릴 수 없습니다.";
        }

        @Override
        protected void execute(final SlashCommandEvent event) {
            final User user = event.getUser();

            event.replyEmbeds(new EmbedBuilder()
                            .setTitle("계정 초기화 확인")
                            .setColor(DiaColor.SEVERE)
                            .setAuthor(user.getAsTag(), null, user.getAvatarUrl())
                            .setDescription("당신의 계정을 초기화 합니다.\n초기화를 원하시면 아래의 버튼을 눌러주세요.")
                            .addField("주의!", "**되돌릴 수 없습니다.**", false)
                            .setFooter("이렇게까지 만들었는데도 날려먹으면 네 탓!!!")
                            .build())
                    .addActionRow(
                            Button.danger("explosion_reset", "초기화"),
                            Button.danger("explosion_delete", "삭제"))
                    .setEphemeral(true)
                    .queue();
        }

    }
}
