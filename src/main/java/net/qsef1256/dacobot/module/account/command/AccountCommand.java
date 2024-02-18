package net.qsef1256.dacobot.module.account.command;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.qsef1256.dacobot.command.DacoCommand;
import net.qsef1256.dacobot.game.explosion.controller.UserController;
import net.qsef1256.dacobot.game.explosion.v2.cash.CashService;
import net.qsef1256.dacobot.module.account.controller.AccountController;
import net.qsef1256.dacobot.module.account.entity.UserEntity;
import net.qsef1256.dacobot.module.account.exception.DacoAccountException;
import net.qsef1256.dacobot.setting.constants.DiaColor;
import net.qsef1256.dacobot.setting.constants.DiaImage;
import net.qsef1256.dacobot.setting.constants.DiaInfo;
import net.qsef1256.dacobot.ui.DiaEmbed;
import net.qsef1256.dialib.common.ResultSwitch;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class AccountCommand extends DacoCommand {

    public AccountCommand(@NotNull RegisterCommand registerCommand,
                          @NotNull StatusCommand statusCommand,
                          @NotNull ResetCommand resetCommand) {
        name = "계정";
        help = "계정을 관리합니다.";

        children = new SlashCommand[]{
                registerCommand,
                statusCommand,
                resetCommand
        };
    }

    @Override
    protected void runCommand(@NotNull SlashCommandEvent event) {
        callNeedSubCommand();
    }

    @Component
    public static class RegisterCommand extends DacoCommand {

        private final AccountController accountController;
        private final UserController userController;

        public RegisterCommand(@NotNull AccountController accountController,
                               @NotNull UserController userController) {
            this.accountController = accountController;
            this.userController = userController;

            name = "등록";
            help = "폭발물 취급 허가를 얻습니다.";
        }

        @Override
        protected void runCommand(@NotNull SlashCommandEvent event) {
            User user = event.getUser();

            event.deferReply().queue(callback -> {
                try {
                    accountController.register(user.getIdLong());
                    userController.register(user.getIdLong());
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
                    log.error("계정 등록 실패", e);
                }
            });
        }

    }

    @Component
    public static class StatusCommand extends DacoCommand {

        private final CashService cashService;
        private final AccountController accountController;

        public StatusCommand(@NotNull AccountController accountController,
                             @NotNull CashService cashService) {
            this.accountController = accountController;
            this.cashService = cashService;

            name = "확인";
            help = "계정 상태를 확인합니다. 아니면 돈 자랑을 하거나...";
        }

        @Override
        protected void runCommand(@NotNull SlashCommandEvent event) {
            User user = event.getUser();

            try {
                UserEntity userData = accountController.getAccount(user.getIdLong());
                long cash = cashService.getCash(user.getIdLong());

                event.replyEmbeds(new EmbedBuilder()
                        .setTitle("계정 정보")
                        .setColor(DiaColor.INFO)
                        .setAuthor(DiaInfo.BOT_NAME, null, DiaImage.MAIN_THUMBNAIL)
                        .setThumbnail(user.getEffectiveAvatarUrl())
                        .addField("닉네임", user.getName(), true)
                        .addField("가입 일자", userData.getRegisterTime().format(DateTimeFormatter.ISO_LOCAL_DATE), true)
                        .addField("계정 상태", userData.getStatus(), true)
                        .addField("돈", cash + " 코인", false)
                        .setFooter(getFooter(cash))
                        .build()).queue();

            } catch (RuntimeException e) {
                event.replyEmbeds(DiaEmbed.error("계정 정보 확인 실패", null, e, user)
                        .setFooter("계정 신청은 /계정 등록")
                        .build()).queue();

                if (e instanceof DacoAccountException) return;
                log.error("계정 정보 확인 실패", e);
            }
        }

        private String getFooter(long cash) {
            return ResultSwitch.get(cash, String.class)
                    .caseCondition(cash > 1, "한푼 두푼 모으다 보면 많아질꺼에요.")
                    .caseCondition(cash > 100, "멋지네요! 100 코인 돌파!")
                    .caseCondition(cash > 500, "500 코인 돌파!")
                    .caseCondition(cash > 5000, "이제 5천원 짜리 계정이네요. 잠깐.. 현실 돈이던가")
                    .caseCondition(cash > 10000, "1만을 찍었어요. 더 벌어보자구요.")
                    .caseCondition(cash > 50000, "돈이 많군요!")
                    .defaultResult("아직 돈이 없군요. 돈을 벌어보세요!");
        }

    }

    @Component
    public static class ResetCommand extends DacoCommand {

        public ResetCommand() {
            name = "초기화";
            help = "당신의 계정을 폭파시킵니다. 주의! 되돌릴 수 없습니다.";
        }

        @Override
        protected void runCommand(@NotNull SlashCommandEvent event) {
            User user = event.getUser();

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
