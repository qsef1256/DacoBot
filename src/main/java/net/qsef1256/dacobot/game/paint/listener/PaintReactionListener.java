package net.qsef1256.dacobot.game.paint.listener;

import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.qsef1256.dacobot.game.paint.enums.ColorEmoji;
import net.qsef1256.dacobot.game.paint.model.PaintDrawer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;

@Component
public class PaintReactionListener extends ListenerAdapter {

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        AtomicReference<User> user = new AtomicReference<>();
        event.retrieveUser().queue(callback -> user.set(event.getUser()));
        if (user.get() == null) return;

        long messageIdLong = event.getMessageIdLong();
        long userId = user.get().getIdLong();
        MessageReaction reaction = event.getReaction();

        ColorEmoji color = getPixelColor(userId, messageIdLong, reaction);
        if (color == null) return;

        event.retrieveMessage().queue(callback -> {
            ColorEmoji selectedColor = PaintDrawer.getColor(userId);
            if (selectedColor != null) {
                Emoji emoji = Emoji.fromUnicode(selectedColor.getEmoji());
                callback.removeReaction(emoji, user.get()).queue();
            }

            PaintDrawer.setColor(userId, color);
        });
    }

    @Nullable
    private ColorEmoji getPixelColor(long userId, long messageIdLong, MessageReaction reaction) {
        Long drawerId = PaintDrawer.getDrawerId(userId);
        if (drawerId == null) return null;
        if (messageIdLong != drawerId) return null;

        return ColorEmoji.findByEmoji(reaction.getEmoji().getAsReactionCode());
    }

    @Override
    public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event) {
        AtomicReference<User> user = new AtomicReference<>();
        event.retrieveUser().queue(callback -> user.set(event.getUser()));
        if (user.get() == null) return;

        long messageIdLong = event.getMessageIdLong();
        long userId = user.get().getIdLong();
        MessageReaction reaction = event.getReaction();

        ColorEmoji selectedColor = PaintDrawer.getColor(userId);
        ColorEmoji removedColor = getPixelColor(userId, messageIdLong, reaction);
        if (removedColor == null) return;
        if (selectedColor == removedColor)
            PaintDrawer.clearColor(userId);
    }

}
