package net.qsef1256.dacobot.service.message.type;

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;

// TODO
public class ThreadMessage implements AbstractMessage, Controllable {

    @Override
    public void move(MessageChannel channel) {
        throw new UnsupportedOperationException("this Message is TODO");
    }

    @Override
    public void edit(MessageEditBuilder content) {
        throw new UnsupportedOperationException("this Message is TODO");
    }

    @Override
    public void send() {
        throw new UnsupportedOperationException("this Message is TODO");
    }

    @Override
    public void invalidate() {
        throw new UnsupportedOperationException("this Message is TODO");
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("this Message is TODO");
    }

    @Override
    public boolean exists() {
        return false;
    }

}
