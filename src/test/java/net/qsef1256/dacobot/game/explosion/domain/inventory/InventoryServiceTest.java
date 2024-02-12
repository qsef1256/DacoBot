package net.qsef1256.dacobot.game.explosion.domain.inventory;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

// TODO: fill test
@SpringBootTest
class InventoryServiceTest {

    @Autowired
    private InventoryService inventory;
    private final long TEST_ID = 419761037861060619L;

    @Test
    void getItems() {
        assertDoesNotThrow(() -> {
            inventory.getItems(TEST_ID);
        });
    }

    @Test
    void setItem() {

    }

    @Test
    void addItem() {
        inventory.addItem(TEST_ID, 1, 1);

        assertTrue(inventory.getItem(TEST_ID, 1).amount() >= 1);
    }

    @Test
    void createItem() {

    }

    @Test
    void removeItem() {
        inventory.removeItem(TEST_ID, 1);
    }

    @Test
    void clearItem() {
        inventory.clearItem(TEST_ID, 1);

        assertEquals(0, (int) inventory.getItem(TEST_ID, 1).amount());
    }

}