package net.qsef1256.dacobot.service.message.snowflake;

import com.sun.jdi.request.DuplicateRequestException;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.qsef1256.dacobot.service.key.MultiUserKey;
import net.qsef1256.dacobot.service.key.SingleUserKey;
import net.qsef1256.dacobot.service.message.MessageAPI;
import net.qsef1256.dacobot.service.message.data.MessageData;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Slf4j
class MessageAPITest {

    private static MultiUserKey key1, key2, key3, key4, key5, key6;
    private static SingleUserKey key7, key8, key9, key10;
    private MessageChannel channel;

    @BeforeAll
    static void setUp() {
        User user1 = Mockito.mock(User.class);
        User user2 = Mockito.mock(User.class);
        User user3 = Mockito.mock(User.class);

        when(user1.getIdLong()).thenReturn(1L);
        when(user2.getIdLong()).thenReturn(2L);
        when(user3.getIdLong()).thenReturn(3L);

        // Same
        key1 = new MultiUserKey("test", Set.of(user1, user2, user3));
        key2 = new MultiUserKey("test", Set.of(user3, user1, user2));
        key3 = new MultiUserKey("test", Set.of(user1, user2, user3));

        // Diff
        key4 = new MultiUserKey("testDiff", Set.of(user1, user2, user3));
        key5 = new MultiUserKey("test", Set.of(user1, user2));
        key6 = new MultiUserKey("test", Set.of());

        // Same
        key7 = new SingleUserKey("test", user1);
        key8 = new SingleUserKey("test", user1);

        // Diff
        key9 = new SingleUserKey("test", user2);
        key10 = new SingleUserKey("testDiff", user1);
    }

    @BeforeEach
    public void clear() {
        MessageAPI.clear();
    }

    @Test
    void same() {
        assertEquals(key1, key2);
    }

    @Test
    void add() {
        channel = Mockito.mock(MessageChannel.class);

        MessageAPI.add(key1, createData(1L));

        assertThrows(DuplicateRequestException.class, () -> MessageAPI.add(key2, createData(2L)));
        assertThrows(DuplicateRequestException.class, () -> MessageAPI.add(key3, createData(3L)));
        assertDoesNotThrow(() -> MessageAPI.add(key4, createData(4L)));
        assertDoesNotThrow(() -> MessageAPI.add(key5, createData(5L)));
        assertDoesNotThrow(() -> MessageAPI.add(key6, createData(6L)));

        MessageAPI.add(key7, createData(7L));
        assertThrows(DuplicateRequestException.class, () -> MessageAPI.add(key8, createData(8L)));
        assertDoesNotThrow(() -> MessageAPI.add(key9, createData(9L)));
        assertDoesNotThrow(() -> MessageAPI.add(key10, createData(10L)));
    }

    @Test
    void get() {
        MessageAPI.add(key1, createData(1L));
        assertEquals(createData(1L), MessageAPI.get(key2));

        MessageAPI.add(key7, createData(7L));
        assertEquals(createData(7L), MessageAPI.get(key8));
        assertThrows(NoSuchElementException.class, () -> MessageAPI.get(key9));
        assertThrows(NoSuchElementException.class, () -> MessageAPI.get(key10));
    }

    @Contract("_ -> new")
    private @NotNull MessageData createData(long snowflake) {
        return new MessageData(snowflake, channel);
    }

}
