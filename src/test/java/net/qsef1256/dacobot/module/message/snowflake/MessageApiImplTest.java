package net.qsef1256.dacobot.module.message.snowflake;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.qsef1256.dacobot.module.common.key.MultiUserKey;
import net.qsef1256.dacobot.module.common.key.SingleUserKey;
import net.qsef1256.dacobot.module.message.MessageApiImpl;
import net.qsef1256.dacobot.module.message.data.MessageData;
import net.qsef1256.dacobot.module.message.exception.MessageApiException;
import net.qsef1256.dialib.util.ToStringUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class MessageApiImplTest {

    @Autowired
    private MessageApiImpl messageApi;

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
        messageApi.remove(key1);
        messageApi.remove(key2);
        messageApi.remove(key3);
        messageApi.remove(key4);
        messageApi.remove(key5);
        messageApi.remove(key6);
        messageApi.remove(key7);
        messageApi.remove(key8);
        messageApi.remove(key9);
        messageApi.remove(key10);
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
    @SuppressWarnings("java:S5778")
    void add() {
        channel = Mockito.mock(MessageChannel.class);

        messageApi.add(key1, createData(1L));

        log.info(key1.toString());
        log.info(key2.toString());
        log.info(ToStringUtil.mapToString(messageApi.getAsMap()));
        assertThrows(MessageApiException.class, () -> messageApi.add(key2, createData(2L)));
        assertThrows(MessageApiException.class, () -> messageApi.add(key3, createData(3L)));
        assertDoesNotThrow(() -> messageApi.add(key4, createData(4L)));
        assertDoesNotThrow(() -> messageApi.add(key5, createData(5L)));
        assertDoesNotThrow(() -> messageApi.add(key6, createData(6L)));

        messageApi.add(key7, createData(7L));
        assertThrows(MessageApiException.class, () -> messageApi.add(key8, createData(8L)));
        assertDoesNotThrow(() -> messageApi.add(key9, createData(9L)));
        assertDoesNotThrow(() -> messageApi.add(key10, createData(10L)));
    }

    @Test
    @Order(3)
    void get() {
        messageApi.add(key1, createData(1L));
        assertEquals(createData(1L), messageApi.get(key2));

        messageApi.add(key7, createData(7L));
        assertEquals(createData(7L), messageApi.get(key8));
        assertThrows(NoSuchElementException.class, () -> messageApi.get(key9));
        assertThrows(NoSuchElementException.class, () -> messageApi.get(key10));
    }

    @NotNull
    @Contract("_ -> new")
    private MessageData createData(long snowflake) {
        return new MessageData(snowflake, channel);
    }

}
