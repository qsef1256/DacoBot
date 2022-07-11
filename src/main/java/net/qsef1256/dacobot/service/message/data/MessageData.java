package net.qsef1256.dacobot.service.message.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import net.dv8tion.jda.api.entities.MessageChannel;

@Data
@AllArgsConstructor
public class MessageData {

    private Long messageId;
    private MessageChannel channel;

}
