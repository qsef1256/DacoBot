package net.qsef1256.dacobot.module.account.button;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.qsef1256.dacobot.core.ui.button.DacoButton;
import net.qsef1256.dacobot.module.account.user.UserController;
import net.qsef1256.dacobot.setting.constants.DiaColor;
import net.qsef1256.dacobot.ui.DiaEmbed;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Slf4j
@Component
public class ResetButton extends DacoButton {

    private final UserController userController;

    protected ResetButton(@NotNull UserController userController) {
        super(Button.danger("account_reset", "초기화"));

        this.userController = userController;
    }

    @Override
    protected void runButton(@NotNull ButtonInteractionEvent event) {
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
            log.error("Failed to reset user account %s(%s)".formatted(
                    user.getName(),
                    user.getIdLong()), e);
        }

        event.editButton(event.getButton().asDisabled()).queue();
    }

}
