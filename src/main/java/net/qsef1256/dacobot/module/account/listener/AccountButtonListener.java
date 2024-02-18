package net.qsef1256.dacobot.module.account.listener;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.qsef1256.dacobot.game.explosion.controller.UserController;
import net.qsef1256.dacobot.module.account.command.AccountCommand;
import net.qsef1256.dacobot.module.account.controller.AccountController;
import net.qsef1256.dacobot.module.account.exception.DacoAccountException;
import net.qsef1256.dacobot.setting.constants.DiaColor;
import net.qsef1256.dacobot.ui.DiaEmbed;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
public class AccountButtonListener extends ListenerAdapter {

    private final AccountController accountController;
    private final UserController userController;

    public AccountButtonListener(@NotNull AccountController accountController,
                                 @NotNull UserController userController) {
        this.accountController = accountController;
        this.userController = userController;
    }

    /**
     * Event for AccountCommand#ResetCommand
     *
     * @see AccountCommand
     */
    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) { // TODO: refactoring to Account model
        switch (event.getComponentId()) {
            case "account_reset" -> {
                User user = event.getUser();

                try {
                    userController.reset(user.getIdLong());
                    event.replyEmbeds(new EmbedBuilder()
                            .setTitle("계정 초기화 됨")
                            .setColor(DiaColor.SUCCESS)
                            .setAuthor(user.getName(), null, user.getEffectiveAvatarUrl())
                            .setDescription("정상적으로 초기화 되었습니다.")
                            .setFooter("당신의 계정 한줄기 빛으로 대체")
                            .build()).queue();

                    event.editButton(event.getButton().asDisabled()).queue();
                } catch (RuntimeException e) {
                    event.replyEmbeds(DiaEmbed.error("계정 초기화 실패", null, e, user)
                            .setFooter("예술의 끝으로 계정을 대폭발 시키려고 하지만 누군가가 막고 있군요...")
                            .build()).queue();

                    if (e instanceof NoSuchElementException) return; // TODO: anti-pattern
                    e.printStackTrace();
                }

                event.editButton(event.getButton().asDisabled()).queue();
            }
            case "account_delete" -> {
                User user = event.getUser();

                try {
                    accountController.delete(user.getIdLong());
                    event.replyEmbeds(new EmbedBuilder()
                            .setTitle("계정 삭제 됨")
                            .setColor(DiaColor.SUCCESS)
                            .setAuthor(user.getName(), null, user.getEffectiveAvatarUrl())
                            .setDescription("정상적으로 삭제 되었습니다.")
                            .setFooter("으음... 다시 오실꺼죠?")
                            .build()).queue();

                    event.editButton(event.getButton().asDisabled()).queue();
                } catch (RuntimeException e) {
                    event.replyEmbeds(DiaEmbed.error("계정 삭제 실패", null, e, user)
                            .setFooter("문제가 있을시 관리자를 불러주세요.")
                            .build()).queue();

                    if (e instanceof NoSuchElementException || e instanceof DacoAccountException) return;
                    e.printStackTrace();
                }

                event.editButton(event.getButton().asDisabled()).queue();
            }
            default -> {
            }
        }
    }

}
