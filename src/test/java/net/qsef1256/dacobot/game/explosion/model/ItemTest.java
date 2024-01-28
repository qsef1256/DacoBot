package net.qsef1256.dacobot.game.explosion.model;

import net.qsef1256.dacobot.game.explosion.domain.item.ItemOld;
import net.qsef1256.dacobot.game.explosion.domain.itemtype.ItemTypeEntity;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

class ItemTest {

    // FIXME: assertion need
    @Test
    void testItem() {
        ItemOld item = ItemOld.fromId(1);

        ItemTypeEntity itemType = item.getItemType();
        if (itemType == null) throw new NoSuchElementException("아이템 없어요");
        System.out.println("Item name: " + itemType.getItemName());
        System.out.println("Item icon: " + itemType.getItemIcon());
        System.out.println("Item desc: " + itemType.getDescription());
        System.out.println("Item rank: " + itemType.getItemRank().getTitle());
    }

}
