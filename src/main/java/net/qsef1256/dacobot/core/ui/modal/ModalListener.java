package net.qsef1256.dacobot.core.ui.modal;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ModalListener extends ListenerAdapter {

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        String modalId = event.getModalId();

        // TODO
    }

}