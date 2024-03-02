package net.qsef1256.dacobot.core.ui.button;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.dv8tion.jda.internal.interactions.component.ButtonImpl;
import org.jetbrains.annotations.NotNull;

public abstract class DacoButton extends ButtonImpl {

    protected DacoButton(DataObject data) {
        super(data);
    }

    protected DacoButton(String id,
                         String label,
                         ButtonStyle style,
                         boolean disabled,
                         Emoji emoji) {
        super(id, label, style, disabled, emoji);
    }

    protected DacoButton(String id,
                         String label,
                         ButtonStyle style,
                         String url,
                         boolean disabled,
                         Emoji emoji) {
        super(id, label, style, url, disabled, emoji);
    }

    /**
     * constructor for use {@link Button}'s factory methods
     * <pre>{@code
     *
     * protected DeleteButton() {
     *     super(Button.danger("account_delete", "삭제"));
     * }
     * }</pre>
     *
     * @param button Button
     */
    protected DacoButton(@NotNull Button button) {
        super(button.getId(),
                button.getLabel(),
                button.getStyle(),
                button.getUrl(),
                button.isDisabled(),
                button.getEmoji());
    }

    protected abstract void runButton(@NotNull ButtonInteractionEvent event);

}
