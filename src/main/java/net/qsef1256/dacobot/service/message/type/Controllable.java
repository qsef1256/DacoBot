package net.qsef1256.dacobot.service.message.type;

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;

public interface Controllable extends AbstractMessage {

    /**
     * 관리 대상에서 삭제합니다. 메시지는 삭제되지 않습니다.
     */
    void invalidate();

    void edit(MessageEditBuilder content);

    void remove();

    void move(MessageChannel channel);

    boolean exists();

}
