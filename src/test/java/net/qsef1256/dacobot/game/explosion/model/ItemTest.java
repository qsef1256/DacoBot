package net.qsef1256.dacobot.game.explosion.model;

import net.qsef1256.dacobot.game.explosion.data.ItemTypeEntity;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

public class ItemTest {

    @Test
    public void testItem() {
        Item item = Item.fromId(1);

        ItemTypeEntity itemType = item.getItemType();
        if (itemType == null) throw new NoSuchElementException("아이템 없어요");
        System.out.println("Item name: " + itemType.getItemName());
        System.out.println("Item icon: " + itemType.getItemIcon());
        System.out.println("Item desc: " + itemType.getDescription());
        System.out.println("Item rank: " + itemType.getItemRank().getTitle());
    }

}