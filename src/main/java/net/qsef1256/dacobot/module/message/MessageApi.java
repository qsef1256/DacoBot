package net.qsef1256.dacobot.module.message;

import net.qsef1256.dacobot.module.common.key.ManagedKey;
import net.qsef1256.dacobot.module.message.data.MessageData;
import org.jetbrains.annotations.TestOnly;

public interface MessageApi {

    void add(ManagedKey key, MessageData snowflake);

    MessageData get(ManagedKey key);

    void remove(ManagedKey key);

    boolean has(ManagedKey key);

    void refresh(ManagedKey key);

    @TestOnly
    void clear();

}
