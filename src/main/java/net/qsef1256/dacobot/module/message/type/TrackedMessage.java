package net.qsef1256.dacobot.module.message.type;

import com.sun.jdi.request.DuplicateRequestException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;
import net.qsef1256.dacobot.module.common.key.ManagedKey;
import net.qsef1256.dacobot.module.message.MessageApiImpl;
import net.qsef1256.dacobot.module.message.data.MessageData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class TrackedMessage implements AbstractMessage, ControlledMessage, Timed {

    @Setter(onMethod_ = {@Autowired})
    private MessageApiImpl messageApi;

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
        onRemove = null;
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
                messageApi.add(key, new MessageData(result.getIdLong(), channel));
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
        return messageApi.has(key);
    }

    @Override
    public void move(@NotNull MessageChannel channel) {
        remove();

        new TrackedMessage(key, message, channel, onRemove).send();
    }

    @Override
    public void invalidate() {
        messageApi.remove(key);
    }

    @Override
    public void remove() {
        MessageData messageData = getMessageData();
        messageData.getChannel().deleteMessageById(messageData.getMessageId()).queue();

        invalidate();
    }

    protected MessageData getMessageData() {
        return messageApi.get(key);
    }

    @Override
    public void refresh() {
        messageApi.refresh(key);
    }

    @Override
    public void onTimeout() {
        messageApi.get(key).getOnRemove().run();
    }

}
