package net.qsef1256.dacobot.module.request.listener;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.qsef1256.dacobot.module.request.model.RequestAPI;
import net.qsef1256.dacobot.ui.DiaEmbed;
import org.jetbrains.annotations.NotNull;

public class RequestButtonListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(final @NotNull ButtonInteractionEvent event) {
        switch (event.getComponentId()) {
            case "request_accept" -> {
                User eventUser = event.getUser();

                try {
                    RequestAPI.accept(event.getMessage().getIdLong(), eventUser.getIdLong());

                    event.deferEdit().queue();
                } catch (RuntimeException e) {
                    event.replyEmbeds(DiaEmbed.error("요청 실패", null, e, eventUser).build()).queue();
                }
            }

            case "request_deny" -> {
                User eventUser = event.getUser();

                try {
                    RequestAPI.deny(event.getMessage().getIdLong(), eventUser.getIdLong());

                    event.deferEdit().queue();
                } catch (RuntimeException e) {
                    event.replyEmbeds(DiaEmbed.error("요청 실패", null, e, eventUser).build()).queue();
                }
            }

            default -> {
            }
        }
    }

}
