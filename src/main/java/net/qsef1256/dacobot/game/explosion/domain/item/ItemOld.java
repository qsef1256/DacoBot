package net.qsef1256.dacobot.game.explosion.domain.item;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.qsef1256.dacobot.database.DaoCommonJpa;
import net.qsef1256.dacobot.database.DaoCommonJpaImpl;
import net.qsef1256.dacobot.game.explosion.domain.inventory.InventoryEntity;
import net.qsef1256.dacobot.game.explosion.domain.itemtype.ItemTypeEntity;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

@Deprecated
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemOld {

    protected static final DaoCommonJpa<InventoryEntity, Long> dao = new DaoCommonJpaImpl<>(InventoryEntity.class);
    protected static final DaoCommonJpa<ItemTypeEntity, Integer> itemDao = new DaoCommonJpaImpl<>(ItemTypeEntity.class);

    @Getter
    @Setter
    private ItemEntity itemEntity;

    private ItemOld(int itemId) {
        if (!itemDao.existsById(itemId))
            throw new IllegalArgumentException("ID: %s 아이템을 찾을 수 없습니다.".formatted(itemId));
        itemEntity = new ItemEntity(itemDao.findById(itemId));
        itemEntity.setAmount(1);
    }

    private ItemOld(int itemId, int amount) {
        this(itemId);
        itemEntity.setAmount(amount);
    }

    @NotNull
    public static ItemOld fromId(int itemId) {
        return new ItemOld(itemId);
    }

    @NotNull
    public static ItemOld fromId(int itemId, int amount) {
        return new ItemOld(itemId, amount);
    }

    @NotNull
    public static ItemOld fromUser(long userId, int itemId) {
        ItemEntity userItem = dao.findById(userId).getItem(itemId);
        ItemOld item = new ItemOld(itemId);
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
