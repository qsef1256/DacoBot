package net.qsef1256.dacobot.service.diapedia;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class DiapediaTest {

    @Test
    void index() {
        assertDoesNotThrow(() -> System.out.println(Diapedia.getInstance()
                .index()
                .toData()));
    }

    @Test
    void search() {
        assertDoesNotThrow(() -> System.out.println(Diapedia.getInstance()
                .search("Language")
                .toData()));
    }

}