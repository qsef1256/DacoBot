package net.qsef1256.dacobot.service.message.type;

import com.sun.jdi.request.DuplicateRequestException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;
import net.qsef1256.dacobot.service.key.ManagedKey;
import net.qsef1256.dacobot.service.message.MessageApiImpl;
import net.qsef1256.dacobot.service.message.data.MessageData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Slf4j
public class TrackedMessage implements AbstractMessage, Controllable, Timed {

    @Getter
    protected final ManagedKey key;
    @Getter
    protected final MessageCreateBuilder message;
    @Nullable
    protected Runnable onRemove;

    private final MessageChannel channel;

    public TrackedMessage(@NotNull ManagedKey key,
                          @NotNull MessageCreateBuilder message,
                          @NotNull MessageChannel channel) {
        this.key = key;
        this.message = message;
        this.channel = channel;
        this.onRemove = null;
    }

    public TrackedMessage(@NotNull ManagedKey key,
                          @NotNull MessageCreateBuilder message,
                          @NotNull MessageChannel channel,
                          @Nullable Runnable onRemove) {
        this(key, message, channel);
        this.onRemove = onRemove;
    }

    @Override
    public void send() {
        if (exists())
            throw new DuplicateRequestException("이미 메시지가 있습니다: " + key);
        else
            channel.sendMessage(message.build()).queue(result -> {
                getMessageApi().add(key, new MessageData(result.getIdLong(), channel));
                log.info("result");
            });
    }

    @Override
    public void edit(@NotNull MessageEditBuilder content) {
        MessageData messageData = getMessageData();
        messageData.getChannel().editMessageById(messageData.getMessageId(), content.build()).queue();
    }

    @Override
    public boolean exists() {
        return getMessageApi().has(key);
    }

    @Override
    public void move(@NotNull MessageChannel channel) {
        remove();

        new TrackedMessage(key, message, channel, onRemove).send();
    }

    @Override
    public void invalidate() {
        getMessageApi().remove(key);
    }

    @Override
    public void remove() {
        MessageData messageData = getMessageData();
        messageData.getChannel().deleteMessageById(messageData.getMessageId()).queue();

        invalidate();
    }

    protected MessageData getMessageData() {
        return getMessageApi().get(key);
    }

    @Override
    public void refresh() {
        getMessageApi().refresh(key);
    }

    @Override
    public void onTimeout() {
        getMessageApi().get(key).getOnRemove().run();
    }

    private static MessageApiImpl getMessageApi() {
        return MessageApiImpl.getInstance();
    }

}
