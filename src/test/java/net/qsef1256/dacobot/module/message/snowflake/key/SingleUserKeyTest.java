package net.qsef1256.dacobot.module.message.snowflake.key;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.qsef1256.dacobot.module.common.key.SingleUserKey;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(MockitoExtension.class)
class SingleUserKeyTest {

    private static SingleUserKey key1, key2, key3, key4, key5, key6;

    @BeforeAll
    static void setUp() {
        UserSnowflake user1 = User.fromId(1);
        UserSnowflake user2 = User.fromId(2);
        UserSnowflake user3 = User.fromId(3);

        // Same
        key1 = new SingleUserKey("test", user1);
        key2 = new SingleUserKey("test", user1);

        // Diff
        key3 = new SingleUserKey("test", user2);
        key4 = new SingleUserKey("test", user2);
        key5 = new SingleUserKey("testDiff", user1);
        key6 = new SingleUserKey("test", user3);
    }

    @Test
    void testEquals() {
        assertEquals(key1, key2);
        assertNotEquals(key1, key3);
        assertNotEquals(key1, key4);
        assertNotEquals(key1, key5);
        assertNotEquals(key1, key6);
    }

    @Test
    void testHashCode() {
        assertEquals(key1.hashCode(), key2.hashCode());
        assertNotEquals(key1.hashCode(), key3.hashCode());
        assertNotEquals(key1.hashCode(), key4.hashCode());
        assertNotEquals(key1.hashCode(), key5.hashCode());
        assertNotEquals(key1.hashCode(), key6.hashCode());
    }

}
