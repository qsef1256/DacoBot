package net.qsef1256.dacobot.service.message.type;

import lombok.Getter;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;

public class UntrackedMessage implements AbstractMessage {

    @Getter
    private final MessageBuilder message;
    @Getter
    private final MessageChannel channel;

    public UntrackedMessage(MessageBuilder message, MessageChannel channel) {
        this.message = message;
        this.channel = channel;
    }

    @Override
    public void send() {
        channel.sendMessage(message.build()).queue();
    }

}
