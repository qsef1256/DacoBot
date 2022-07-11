package net.qsef1256.dacobot.service.request.listener;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.qsef1256.dacobot.service.notification.DiaEmbed;
import net.qsef1256.dacobot.service.request.model.RequestAPI;
import org.jetbrains.annotations.NotNull;

public class RequestButtonListener extends ListenerAdapter {

    @Override
    public void onButtonClick(final @NotNull ButtonClickEvent event) {
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

        }
    }
}
