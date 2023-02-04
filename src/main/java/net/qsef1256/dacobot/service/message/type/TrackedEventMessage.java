package net.qsef1256.dacobot.service.message.type;

import com.sun.jdi.request.DuplicateRequestException;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;
import net.qsef1256.dacobot.service.key.ManagedKey;
import net.qsef1256.dacobot.service.message.MessageApiImpl;
import net.qsef1256.dacobot.service.message.data.MessageData;
import org.jetbrains.annotations.NotNull;

/**
 * SlashCommand 등에서 사용하는 Interaction 을 위한 확장
 */
public class TrackedEventMessage extends TrackedMessage implements Timed {

    private final IReplyCallback event;

    public TrackedEventMessage(ManagedKey key, MessageCreateBuilder message, @NotNull IReplyCallback event) {
        super(key, message, event.getMessageChannel());

        this.event = event;
    }

    public TrackedEventMessage(ManagedKey key, MessageCreateBuilder message, @NotNull IReplyCallback event, Runnable onRemove) {
        super(key, message, event.getMessageChannel(), onRemove);

        this.event = event;
    }

    @Override
    public void send() {
        if (exists()) throw new DuplicateRequestException("이미 메시지가 있습니다: " + key);

        event.reply(message.build()).queue(result -> result.retrieveOriginal().queue(
                original -> getMessageApi().add(key, new MessageData(original.getIdLong(), event.getMessageChannel()))));
    }

    public void edit(@NotNull ReplyMessage replyMessage, @NotNull MessageEditBuilder content) {
        MessageData messageData = getMessageData();

        messageData.getChannel()
                .editMessageById(getMessageApi().get(key).getMessageId(), content.build())
                .queue();
        replyMessage.send();
    }

    @Override
    public void edit(@NotNull MessageEditBuilder content) {
        edit(ReplyMessage.builder(event).content(
                new MessageCreateBuilder().setContent("메시지 수정이 완료 되었습니다.").build()
        ).debug(true).build(), content);
    }

    private void delete() {
        MessageData messageData = getMessageData();
        messageData.getChannel().deleteMessageById(messageData.getMessageId()).queue();

        invalidate();
    }

    public void remove(@NotNull ReplyMessage replyMessage) {
        delete();

        replyMessage.send();
    }

    @Override
    public void remove() {
        remove(ReplyMessage.builder(event).content(
                new MessageCreateBuilder().setContent("메시지 삭제가 완료되었습니다.").build()
        ).build());
    }

    @Override
    public void move(@NotNull MessageChannel channel) {
        MessageData messageData = getMessageData();

        Message message = messageData.getChannel()
                .retrieveMessageById(messageData.getMessageId())
                .complete();

        MessageCreateBuilder messageBuilder = MessageCreateBuilder.fromMessage(message);
        delete();

        new TrackedEventMessage(key, messageBuilder, event).send();
    }

    private static MessageApiImpl getMessageApi() {
        return MessageApiImpl.getInstance();
    }

}
