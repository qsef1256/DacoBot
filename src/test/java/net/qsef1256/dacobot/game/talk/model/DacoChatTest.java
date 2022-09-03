package net.qsef1256.dacobot.game.talk.model;

import org.junit.jupiter.api.Test;

import javax.management.openmbean.KeyAlreadyExistsException;

public class DacoChatTest {

    @Test
    public void testAddTalk() {
        try {
            DacoChat.getInstance().addCategory("다코", "안?뇽");
        } catch (KeyAlreadyExistsException ignored) {
        }
    }

}
