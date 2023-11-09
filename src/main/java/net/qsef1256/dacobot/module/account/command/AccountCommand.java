package net.qsef1256.dacobot.module.account.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.qsef1256.dacobot.game.explosion.controller.UserController;
import net.qsef1256.dacobot.game.explosion.model.Cash;
import net.qsef1256.dacobot.module.account.controller.AccountController;
import net.qsef1256.dacobot.module.account.data.UserEntity;
import net.qsef1256.dacobot.module.account.exception.DacoAccountException;
import net.qsef1256.dacobot.module.account.model.Account;
import net.qsef1256.dacobot.setting.constants.DiaColor;
import net.qsef1256.dacobot.setting.constants.DiaImage;
import net.qsef1256.dacobot.setting.constants.DiaInfo;
import net.qsef1256.dacobot.ui.DiaEmbed;
import net.qsef1256.dacobot.ui.DiaMessage;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;

@Component
public class AccountCommand extends SlashCommand {

    public AccountCommand() {
        name = "계정";
        help = "계정을 관리합니다.";

        children = new SlashCommand[]{
                new RegisterCommand(),
                new StatusCommand(),
                new ResetCommand()
        };
    }

    @Override
    protected void execute(final @NotNull SlashCommandEvent event) {
        event.reply(DiaMessage.needSubCommand(getChildren(), event.getMember())).queue();
    }

    private static class RegisterCommand extends SlashCommand {

        public RegisterCommand() {
            name = "등록";
            help = "폭발물 취급 허가를 얻습니다.";
        }

        @Override
        protected void execute(final @NotNull SlashCommandEvent event) {
            final User user = event.getUser();

            event.deferReply().queue(callback -> {
                try {
                    AccountController.register(user.getIdLong());
                    UserController.register(user.getIdLong());
                    callback.editOriginalEmbeds(new EmbedBuilder()
                            .setTitle("등록 성공")
                            .setColor(DiaColor.SUCCESS)
                            .setAuthor(user.getName(), null, user.getEffectiveAvatarUrl())
                            .setDescription("정상적으로 등록 되었습니다.")
                            .addField("", "시작하기 전에, /규칙 명령어를 확인해주세요.", false)
                            .setFooter("/폭발 커맨드로 게임을 시작하세요!")
                            .build()).queue();
                } catch (RuntimeException e) {
                    callback.editOriginalEmbeds(DiaEmbed.error("등록 실패", null, e, user).build()).queue();

                    if (e instanceof DacoAccountException) return;
                    e.printStackTrace();
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
        protected void execute(final @NotNull SlashCommandEvent event) {
            final User user = event.getUser();

            try {
                final UserEntity userData = new Account(user.getIdLong()).getData();
                final Cash cashData = new Cash(user.getIdLong());
                final long cash = cashData.getCash();

                String footer = "아직 돈이 없군요. 돈을 벌어보세요!";
                footer = getFooter(cash, footer);

                event.replyEmbeds(new EmbedBuilder()
                        .setTitle("계정 정보")
                        .setColor(DiaColor.INFO)
                        .setAuthor(DiaInfo.BOT_NAME, null, DiaImage.MAIN_THUMBNAIL)
                        .setThumbnail(user.getEffectiveAvatarUrl())
                        .addField("닉네임", user.getName(), true)
                        .addField("가입 일자", userData.getRegisterTime().format(DateTimeFormatter.ISO_LOCAL_DATE), true)
                        .addField("계정 상태", userData.getStatus(), true)
                        .addField("돈", cash + " 코인", false)
                        .setFooter(footer)
                        .build()).queue();

            } catch (RuntimeException e) {
                event.replyEmbeds(DiaEmbed.error("계정 정보 확인 실패", null, e, user)
                        .setFooter("계정 신청은 /계정 등록")
                        .build()).queue();

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
        protected void execute(final @NotNull SlashCommandEvent event) {
            final User user = event.getUser();

            event.replyEmbeds(new EmbedBuilder()
                            .setTitle("계정 초기화 확인")
                            .setColor(DiaColor.SEVERE)
                            .setAuthor(user.getName(), null, user.getAvatarUrl())
                            .setDescription("당신의 계정을 초기화 합니다.\n초기화를 원하시면 아래의 버튼을 눌러주세요.")
                            .addField("주의!", "**되돌릴 수 없습니다.**", false)
                            .setFooter("이렇게까지 만들었는데도 날려먹으면 네 탓!!!")
                            .build())
                    .addActionRow(
                            Button.danger("account_reset", "초기화"),
                            Button.danger("account_delete", "삭제"))
                    .setEphemeral(true)
                    .queue();
        }

    }

}
