package net.qsef1256.diabot.game.paint.listener;

import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.qsef1256.diabot.game.paint.model.PaintDrawer;
import net.qsef1256.diabot.game.paint.enums.PixelColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.atomic.AtomicReference;

public class PaintReactionListener extends ListenerAdapter {

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        AtomicReference<User> user = new AtomicReference<>();
        event.retrieveUser().queue(callback -> user.set(event.getUser()));
        if (user.get() == null) return;

        long messageIdLong = event.getMessageIdLong();
        long userId = user.get().getIdLong();
        MessageReaction reaction = event.getReaction();

        PixelColor color = getPixelColor(userId, messageIdLong, reaction);
        if (color == null) return;

        event.retrieveMessage().queue(callback -> {
            PixelColor selectedColor = PaintDrawer.getColor(userId);
            if (selectedColor != null) {
                String emoji = selectedColor.getEmoji();
                callback.removeReaction(emoji, user.get()).queue();
            }

            PaintDrawer.setColor(userId, color);
        });
    }

    @Nullable
    private PixelColor getPixelColor(long userId, long messageIdLong, MessageReaction reaction) {
        Long drawerId = PaintDrawer.getDrawerId(userId);
        if (drawerId == null) return null;
        if (messageIdLong != drawerId) return null;
        MessageReaction.ReactionEmote reactionEmote = reaction.getReactionEmote();

        return PixelColor.findByEmoji(reactionEmote.getEmoji());
    }

    @Override
    public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event) {
        AtomicReference<User> user = new AtomicReference<>();
        event.retrieveUser().queue(callback -> user.set(event.getUser()));
        if (user.get() == null) return;

        long messageIdLong = event.getMessageIdLong();
        long userId = user.get().getIdLong();
        MessageReaction reaction = event.getReaction();

        PixelColor selectedColor = PaintDrawer.getColor(userId);
        PixelColor removedColor = getPixelColor(userId, messageIdLong, reaction);
        if (removedColor == null) return;
        if (selectedColor == removedColor)
            PaintDrawer.clearColor(userId);
    }

}
