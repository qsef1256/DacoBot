package net.qsef1256.dacobot.game.explosion.model;

import net.qsef1256.dacobot.game.explosion.v2.itemtype.ItemType;
import net.qsef1256.dacobot.game.explosion.v2.itemtype.ItemTypeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class ItemTypeTest {

    @Test
    void testItem(@Autowired ItemTypeRepository inventory) {
        assertDoesNotThrow(() -> {
            ItemType itemType = ItemType.fromEntity(inventory.getReferenceById(1));

            System.out.println("Item name: " + itemType.itemName());
            System.out.println("Item icon: " + itemType.itemIcon());
            System.out.println("Item desc: " + itemType.description());
            System.out.println("Item rank: " + itemType.itemRank().getTitle());
        });
    }

}
