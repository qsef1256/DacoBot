package net.qsef1256.dacobot.core.ui.modal;

import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@AllArgsConstructor
public class ModalListener extends ListenerAdapter {

    private final Map<String, DacoModal> modals;

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        String id = event.getModalId();

        DacoModal modal = modals.get(id);
        if (modal != null) modal.runModal(event); // TODO: when modal is null?
    }

}
