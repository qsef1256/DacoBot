package net.qsef1256.dacobot.game.explosion.model;

import net.qsef1256.dacobot.game.explosion.domain.item.Item;
import net.qsef1256.dacobot.game.explosion.domain.item.ItemService;
import net.qsef1256.dacobot.game.explosion.domain.itemtype.ItemType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class ItemTest {

    @Test
    void testItem(@Autowired ItemService itemService) {
        assertDoesNotThrow(() -> {
            Item item = itemService.getItem(1);
            if (item == null) throw new NoSuchElementException("can't find item");

            ItemType itemType = item.itemType();

            System.out.println("Item name: " + itemType.itemName());
            System.out.println("Item icon: " + itemType.itemIcon());
            System.out.println("Item desc: " + itemType.description());
            System.out.println("Item rank: " + itemType.itemRank().getTitle());
        });
    }

}
