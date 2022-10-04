package net.qsef1256.dacobot.service.message.snowflake;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.qsef1256.dacobot.service.key.MultiUserKey;
import net.qsef1256.dacobot.service.key.SingleUserKey;
import net.qsef1256.dacobot.service.message.MessageApiImpl;
import net.qsef1256.dacobot.service.message.data.MessageData;
import net.qsef1256.dacobot.service.message.exception.MessageApiException;
import net.qsef1256.dacobot.util.ToStringUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MessageApiImplTest {

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

    private static MessageApiImpl getInstance() {
        return MessageApiImpl.getInstance();
    }

    @BeforeEach
    public void clear() {
        getInstance().remove(key1);
        getInstance().remove(key2);
        getInstance().remove(key3);
        getInstance().remove(key4);
        getInstance().remove(key5);
        getInstance().remove(key6);
        getInstance().remove(key7);
        getInstance().remove(key8);
        getInstance().remove(key9);
        getInstance().remove(key10);
    }

    @Test
    @Order(1)
    void same() {
        assertEquals(key1, key2);
        assertEquals(key1, key3);
        assertNotEquals(key1, key4);
    }

    @Test
    @Order(2)
    void add() {
        channel = Mockito.mock(MessageChannel.class);

        getInstance().add(key1, createData(1L));

        log.info(key1.toString());
        log.info(key2.toString());
        log.info(ToStringUtil.mapToString(getInstance().getAsMap()));
        assertThrows(MessageApiException.class, () -> getInstance().add(key2, createData(2L)));
        assertThrows(MessageApiException.class, () -> getInstance().add(key3, createData(3L)));
        assertDoesNotThrow(() -> getInstance().add(key4, createData(4L)));
        assertDoesNotThrow(() -> getInstance().add(key5, createData(5L)));
        assertDoesNotThrow(() -> getInstance().add(key6, createData(6L)));

        getInstance().add(key7, createData(7L));
        assertThrows(MessageApiException.class, () -> getInstance().add(key8, createData(8L)));
        assertDoesNotThrow(() -> getInstance().add(key9, createData(9L)));
        assertDoesNotThrow(() -> getInstance().add(key10, createData(10L)));
    }

    @Test
    @Order(3)
    void get() {
        getInstance().add(key1, createData(1L));
        assertEquals(createData(1L), getInstance().get(key2));

        getInstance().add(key7, createData(7L));
        assertEquals(createData(7L), getInstance().get(key8));
        assertThrows(NoSuchElementException.class, () -> getInstance().get(key9));
        assertThrows(NoSuchElementException.class, () -> getInstance().get(key10));
    }

    @Contract("_ -> new")
    private @NotNull MessageData createData(long snowflake) {
        return new MessageData(snowflake, channel);
    }

}
