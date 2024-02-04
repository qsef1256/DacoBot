package net.qsef1256.dacobot.game.explosion.domain.inventory;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

// TODO: fill test
@SpringBootTest
class InventoryServiceTest {

    @Autowired
    private InventoryService inventory;

    @Test
    void getItems() {
        assertDoesNotThrow(() -> {
            inventory.getItems(419761037861060619L);
        });
    }

    @Test
    void setItem() {

    }

    @Test
    void addItem() {

    }

    @Test
    void createItem() {

    }

    @Test
    void removeItem() {

    }

    @Test
    void clearItem() {
    }

}