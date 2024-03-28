package net.qsef1256.dacobot.game.explosion.domain.itemtype;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class ItemTypeTest {

    @Test
    void testItem(@Autowired ItemTypeRepository inventory) {
        assertDoesNotThrow(() -> {
            ItemTypeEntity itemType = inventory.getReferenceById(1);

            System.out.println("Item name: " + itemType.getItemName());
            System.out.println("Item icon: " + itemType.getItemIcon());
            System.out.println("Item desc: " + itemType.getDescription());
            System.out.println("Item rank: " + itemType.getItemRank().getTitle());
        });
    }

}
