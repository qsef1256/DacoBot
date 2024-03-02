package net.qsef1256.dacobot.core.ui.modal;

import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.dv8tion.jda.internal.interactions.modal.ModalImpl;

import java.util.List;
import java.util.Objects;

public abstract class DacoModal extends ModalImpl {

    protected DacoModal(DataObject object) {
        super(object);
    }

    protected DacoModal(String id,
                        String title,
                        List<LayoutComponent> components) {
        super(id, title, components);
    }

    protected abstract void runModal();

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;

        return o.hashCode() == hashCode();
    }

}
