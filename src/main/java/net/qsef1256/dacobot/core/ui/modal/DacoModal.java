package net.qsef1256.dacobot.core.ui.modal;

import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.dv8tion.jda.internal.interactions.modal.ModalImpl;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class DacoModal extends ModalImpl {

    protected DacoModal(DataObject object) {
        super(object);
    }

    protected DacoModal(String id,
                        String title,
                        List<LayoutComponent> components) {
        super(id, title, components);
    }

    protected abstract void runModal(@NotNull ModalInteractionEvent event);

}
