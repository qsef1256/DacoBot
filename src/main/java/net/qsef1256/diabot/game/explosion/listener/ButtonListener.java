package net.qsef1256.diabot.game.explosion.listener;

import com.sun.jdi.request.DuplicateRequestException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.qsef1256.diabot.enums.DiaColor;
import net.qsef1256.diabot.game.explosion.command.ExplosionCommand;
import net.qsef1256.diabot.game.explosion.model.ExplosionGameCore;

import java.sql.SQLException;
import java.util.NoSuchElementException;

public class ButtonListener extends ListenerAdapter {

    /**
     * Event for ExplosionCommand#ResetCommand
     *
     * @see ExplosionCommand
     */
    @Override
    public void onButtonClick(final ButtonClickEvent event) {
        if (event.getComponentId().equals("explosion_reset")) {
            final User user = event.getUser();

            try {
                ExplosionGameCore.reset(user.getIdLong());
                event.replyEmbeds(new EmbedBuilder()
                        .setTitle("계정 초기화 됨")
                        .setColor(DiaColor.SUCCESS)
                        .setAuthor(user.getAsTag(), null, user.getAvatarUrl())
                        .setDescription("정상적으로 초기화 되었습니다.")
                        .setFooter("당신의 계정 한줄기 빛으로 대체")
                        .build()
                ).queue();
            } catch (SQLException | RuntimeException ex) {
                event.replyEmbeds(new EmbedBuilder()
                        .setTitle("계정 초기화 실패")
                        .setColor(DiaColor.FAIL)
                        .setAuthor(user.getAsTag(), null, user.getAvatarUrl())
                        .setDescription(ex.getMessage())
                        .setFooter("예술의 끝으로 계정을 대폭발 시키려고 하지만 누군가가 막고 있군요...")
                        .build()
                ).queue();

                if (ex instanceof NoSuchElementException) return;
                ex.printStackTrace();
            }
        }

        if (event.getComponentId().equals("explosion_delete")) {
            final User user = event.getUser();

            try {
                ExplosionGameCore.delete(user.getIdLong());
                event.replyEmbeds(new EmbedBuilder()
                        .setTitle("계정 삭제 됨")
                        .setColor(DiaColor.SUCCESS)
                        .setAuthor(user.getAsTag(), null, user.getAvatarUrl())
                        .setDescription("정상적으로 삭제 되었습니다.")
                        .setFooter("으음... 다시 오실꺼죠?")
                        .build()
                ).queue();
            } catch (SQLException | RuntimeException ex) {
                event.replyEmbeds(new EmbedBuilder()
                        .setTitle("계정 삭제 실패")
                        .setColor(DiaColor.FAIL)
                        .setAuthor(user.getAsTag(), null, user.getAvatarUrl())
                        .setDescription(ex.getMessage())
                        .setFooter("문제가 있을시 관리자를 불러주세요.")
                        .build()
                ).queue();

                if (ex instanceof NoSuchElementException || ex instanceof DuplicateRequestException) return;
                ex.printStackTrace();
            }
        }

    }

}
