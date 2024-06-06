package net.qsef1256.dacobot.core.ui.button;

import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@AllArgsConstructor
public class ButtonListener extends ListenerAdapter {

    private final Map<String, DacoButton> buttons;

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        String id = event.getComponentId();

        DacoButton button = buttons.get(id);
        if (button != null) button.runButton(event); // TODO: when button is null?
    }

}
