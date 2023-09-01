package net.qsef1256.dacobot.module.message.type;

import lombok.Getter;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;

public class UntrackedMessage implements AbstractMessage {

    @Getter
    private final MessageCreateBuilder message;
    @Getter
    private final MessageChannel channel;

    public UntrackedMessage(MessageCreateBuilder message, MessageChannel channel) {
        this.message = message;
        this.channel = channel;
    }

    @Override
    public void send() {
        channel.sendMessage(message.build()).queue();
    }

}
