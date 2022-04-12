package net.qsef1256.dacobot.system.snowflake;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.dv8tion.jda.api.entities.Message;

// TODO: check this work

/**
 * 고정된 메시지를 이용하는 클래스입니다. 메시지를 삭제하거나 끌어올리기 또는 가져올 수 있습니다.
 */
public abstract class Messaged {

    /**
     * Message Type, User Id, Message Id
     */
    private static final Table<String, Long, Long> idTable = HashBasedTable.create();

    public void sendMessage() {
        getMessage().getChannel().sendMessage(getMessage()).queue(message -> {
            long newMessageId = message.getIdLong();

            removeMessage();
            idTable.put(getMessageType(), getDiscordId(), newMessageId);
        });
    }

    public void editMessage() {
        getMessage().editMessage(getMessage()).queue();
    }

    public abstract String getMessageType();

    public abstract Message getMessage();

    public abstract Long getDiscordId();

    public void removeMessage() {
        idTable.remove(getMessageType(), getDiscordId());
    }

    public Long getMessageId() {
        return idTable.get(getMessageType(), getDiscordId());
    }

}
