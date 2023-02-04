package net.qsef1256.dacobot.game.board.omok.listener;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.qsef1256.dacobot.game.board.omok.model.OmokController;
import net.qsef1256.dacobot.ui.DiaEmbed;
import org.jetbrains.annotations.NotNull;

public class OmokButtonListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        switch (event.getComponentId()) {

            case "omok_confirm" -> {
                User user = event.getUser();

                try {
                    OmokController.confirmStone(user.getIdLong());
                    event.deferEdit().queue();
                    event.getMessage().delete().queue();
                } catch (ErrorResponseException ignored) {
                } catch (RuntimeException e) {
                    event.replyEmbeds(DiaEmbed.error("오목 요청 실패", null, e, user).build()).queue();
                }
            }

            case "omok_cancel" -> {
                User user = event.getUser();

                try {
                    OmokController.cancelStone(user.getIdLong());
                    event.deferEdit().queue();
                    event.getMessage().delete().queue();
                } catch (ErrorResponseException ignored) {
                } catch (RuntimeException e) {
                    event.replyEmbeds(DiaEmbed.error("오목 요청 실패", null, e, user).build()).queue();
                }
            }

        }
    }

}
