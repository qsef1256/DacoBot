package net.qsef1256.diabot.game.explosion.model;

import lombok.Getter;
import net.qsef1256.diabot.database.DaoCommon;
import net.qsef1256.diabot.database.DaoCommonImpl;
import net.qsef1256.diabot.game.explosion.data.InventoryEntity;
import net.qsef1256.diabot.game.explosion.data.ItemEntity;
import net.qsef1256.diabot.game.explosion.data.ItemTypeEntity;
import net.qsef1256.diabot.system.account.data.AccountEntity;

import java.util.Map;

public class Inventory {

    protected final DaoCommon<Long, InventoryEntity> dao = new DaoCommonImpl<>(InventoryEntity.class);

    @Getter
    private final InventoryEntity data;

    public Inventory(long discord_id) {
        data = dao.findById(discord_id);
    }

    public AccountEntity getUser() {
        return data.getDiscordUser();
    }

    public Long getUserId() {
        return getUser().getDiscord_id();
    }

    public Map<Integer, ItemEntity> getItems() {
        return data.getItems();
    }

    public ItemEntity getItem(int itemId) {
        return data.getItem(itemId);
    }

    public void setItem(ItemEntity item) {
        data.setItem(item);
    }

    public void addItem(int itemId) {
        addItem(itemId, 1);
    }

    public void addItem(int itemId, int amount) {
        ItemEntity userItem = data.getItem(itemId);
        Item item = Item.fromId(itemId, amount);

        int maxAmount = item.getMaxAmount();
        int stock = userItem.getAmount();

        int result = stock + amount;
        if (result < maxAmount)
            throw new IllegalArgumentException("%s 아이템을 더 보유할 수 없습니다. 최대: %s".formatted(item.getName(), maxAmount));

        data.setItem(Item.fromId(itemId).getItemEntity());
        update();
    }

    public void removeItem(int itemId) {
        removeItem(itemId, 1);
    }

    public void removeItem(int itemId, int amount) {
        ItemEntity userItem = data.getItem(itemId);
        Item item = Item.fromId(itemId, amount);

        int stock = userItem.getAmount();

        int result = stock - amount;
        if (result < 0)
            throw new IllegalArgumentException("%s 아이템을 더 삭제할 수 없습니다. 재고량: %s".formatted(item.getName(), stock));
        if (result == 0) clearItem(itemId);
        else {
            userItem.setAmount(result);
            data.setItem(userItem);
            update();
        }
    }

    public void clearItem(int itemId) {
        Item userItem = Item.fromUser(getUserId(), itemId);

        data.removeItem(userItem.getItemEntity());
        update();
    }

    private ItemTypeEntity getItemType(Integer itemId) {
        return Item.fromId(itemId).getItemType();
    }

    private void update() {
        dao.update(data);
    }

}
