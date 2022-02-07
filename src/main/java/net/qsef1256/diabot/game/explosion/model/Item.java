package net.qsef1256.diabot.game.explosion.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.qsef1256.diabot.database.DaoCommon;
import net.qsef1256.diabot.database.DaoCommonImpl;
import net.qsef1256.diabot.game.explosion.data.InventoryEntity;
import net.qsef1256.diabot.game.explosion.data.ItemEntity;
import net.qsef1256.diabot.game.explosion.data.ItemTypeEntity;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Item {

    protected static final DaoCommon<Long, InventoryEntity> dao = new DaoCommonImpl<>(InventoryEntity.class);
    protected static final DaoCommon<Integer, ItemTypeEntity> itemDao = new DaoCommonImpl<>(ItemTypeEntity.class);
    @Getter
    @Setter
    private ItemEntity itemEntity;

    private Item(int itemId) {
        if (!itemDao.isExist(itemId)) throw new IllegalArgumentException("ID: %s 아이템을 찾을 수 없습니다.".formatted(itemId));
        itemEntity = new ItemEntity(itemDao.findById(itemId));
        itemEntity.setAmount(1);
    }

    private Item(int itemId, int amount) {
        this(itemId);
        itemEntity.setAmount(amount);
    }

    @NotNull
    public static Item fromId(int itemId) {
        return new Item(itemId);
    }

    @NotNull
    public static Item fromId(int itemId, int amount) {
        return new Item(itemId, amount);
    }

    @NotNull
    public static Item fromUser(long userId, int itemId) {
        ItemEntity userItem = dao.findById(userId).getItem(itemId);
        Item item = new Item(itemId);
        if (userItem == null || userItem.getAmount() == 0)
            throw new NoSuchElementException("%s 아이템을 보유하고 있지 않습니다.".formatted(item.getName()));

        item.setItemEntity(userItem);
        return item;
    }

    public int getItemId() {
        return getItemType().getItemId();
    }

    public String getName() {
        return getItemType().getItemName();
    }

    public int getAmount() {
        return itemEntity.getAmount();
    }

    public int getMaxAmount() {
        return getItemType().getMaxAmount();
    }

    public ItemTypeEntity getItemType() {
        return itemEntity.getItemType();
    }

}
