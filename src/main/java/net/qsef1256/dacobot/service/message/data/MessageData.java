package net.qsef1256.dacobot.service.message.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

@Data
@AllArgsConstructor
public class MessageData {

    private Long messageId;
    private MessageChannel channel;
    private Runnable onRemove;

    public MessageData(Long messageId, MessageChannel channel) {
        this.messageId = messageId;
        this.channel = channel;
    }

}
