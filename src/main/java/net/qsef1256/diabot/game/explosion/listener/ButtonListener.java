package net.qsef1256.diabot.game.explosion.listener;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.qsef1256.diabot.enums.DiaColor;
import net.qsef1256.diabot.game.explosion.command.ExplosionCommand;
import net.qsef1256.diabot.game.explosion.model.ExplosionGameCore;

import java.sql.SQLException;

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
                        .addField(user.getAsTag(), "정상적으로 초기화 되었습니다.", false)
                        .setFooter("당신의 계정 한줄기 빛으로 대체")
                        .build()
                ).queue();
            } catch (SQLException | RuntimeException ex) {
                event.replyEmbeds(new EmbedBuilder()
                        .setTitle("초기화 실패")
                        .setColor(DiaColor.FAIL)
                        .addField(user.getAsTag(), ex.getMessage(), false)
                        .setFooter("예술의 끝으로 계정을 대폭발 시키려고 하지만 누군가가 막고 있군요...")
                        .build()
                ).queue();
                ex.printStackTrace();
            }

        }
    }

}
