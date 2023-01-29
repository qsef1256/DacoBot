package net.qsef1256.dacobot.command.tool;

import net.qsef1256.dacobot.command.tool.moss.MossParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MossParserTest {

    @Test
    void read() {
        assertEquals("sos", MossParser.read("... --- ..."));
        assertEquals("diamond", MossParser.read("-.. .. .- -- --- -. -.."));
    }

    @Test
    void write() {
        assertEquals("... --- ...", MossParser.write("sos"));
        assertEquals(".... . .-.. .-.. --- .-- --- .-. .-.. -..", MossParser.write("Hello world"));
    }

}
