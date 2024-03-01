package net.qsef1256.dacobot.game.explosion.domain.inventory;

import net.qsef1256.dacobot.game.explosion.v2.inventory.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InventoryServiceTest {

    @Autowired
    private InventoryService inventory;
    private final long TEST_ID = 938797562373226577L;

    @BeforeEach
    void setUp() {
        inventory.clearInventory(TEST_ID);
    }

    @Test
    void getItems() {
        assertDoesNotThrow(() -> {
            inventory.getItems(TEST_ID);
        });
    }

    @Test
    void addItem() {
        inventory.addItem(TEST_ID, 1, 1);

        assertTrue(inventory
                .getItem(TEST_ID, 1)
                .getAmount() >= 1);
    }

    @Test
    void setItem() {
        inventory.addItem(TEST_ID, 1, 1);
        inventory.setItem(TEST_ID, 1, 1);

        assertEquals(1, inventory
                .getItem(TEST_ID, 1)
                .getAmount());
    }

    @Test
    void removeItem() {
        inventory.removeItem(TEST_ID, 1);

        assertNull(inventory.getItem(TEST_ID, 1));
    }

    @Test
    void clearItem() {
        inventory.clearItem(TEST_ID, 1);

        assertNull(inventory.getItem(TEST_ID, 1));
    }

}