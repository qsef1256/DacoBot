package net.qsef1256.dacobot.module.message.snowflake.key;

import net.dv8tion.jda.api.entities.User;
import net.qsef1256.dacobot.module.common.key.MultiUserKey;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(MockitoExtension.class)
class MultiUserKeyTest {

    private static MultiUserKey key1, key2, key3, key4, key5, key6;

    @BeforeAll
    static void setUp() {
        User user1 = Mockito.mock(User.class);
        User user2 = Mockito.mock(User.class);
        User user3 = Mockito.mock(User.class);

        // Same
        key1 = new MultiUserKey("test", Set.of(user1, user2, user3));
        key2 = new MultiUserKey("test", Set.of(user3, user1, user2));
        key3 = new MultiUserKey("test", Set.of(user1, user2, user3));

        // Diff
        key4 = new MultiUserKey("testDiff", Set.of(user1, user2, user3));
        key5 = new MultiUserKey("test", Set.of(user1, user2));
        key6 = new MultiUserKey("test", Set.of());
    }

    @Test
    void testEquals() {
        assertEquals(key1, key2);
        assertEquals(key1, key3);
        assertNotEquals(key1, key4);
        assertNotEquals(key1, key5);
        assertNotEquals(key1, key6);
    }

    @Test
    void testHashCode() {
        assertEquals(key1.hashCode(), key2.hashCode());
        assertEquals(key1.hashCode(), key3.hashCode());
        assertNotEquals(key1.hashCode(), key4.hashCode());
        assertNotEquals(key1.hashCode(), key5.hashCode());
        assertNotEquals(key1.hashCode(), key6.hashCode());
    }

}
