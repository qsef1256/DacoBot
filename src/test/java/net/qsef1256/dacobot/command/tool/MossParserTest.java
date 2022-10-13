package net.qsef1256.dacobot.command.tool;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MossParserTest {

    @Test
    void read() {
        assertEquals("sos", MossParser.read("... ___ ..."));
        assertEquals("diamond", MossParser.read("_.. .. ._ __ ___ _. _.."));
    }

    @Test
    void write() {
        assertEquals("... ___ ...", MossParser.write("sos"));
    }

}