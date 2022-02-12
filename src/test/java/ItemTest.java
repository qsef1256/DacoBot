import net.qsef1256.dacobot.game.explosion.data.ItemTypeEntity;
import net.qsef1256.dacobot.game.explosion.model.Item;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

public class ItemTest {

    @Test
    public void testItem() {
        Item item = Item.fromId(1);

        ItemTypeEntity itemType = item.getItemType();
        if (itemType == null) throw new NoSuchElementException("아이템 없어요");
        System.out.println("Item name: " + itemType.getItemName());
        System.out.println("Item desc: " + itemType.getDescription());
        System.out.println("Item rank: " + itemType.getItemRank().getTitle());
    }

}
