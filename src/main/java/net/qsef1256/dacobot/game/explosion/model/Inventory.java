package net.qsef1256.dacobot.game.explosion.model;

import lombok.Getter;
import net.qsef1256.dacobot.database.DaoCommon;
import net.qsef1256.dacobot.database.DaoCommonImpl;
import net.qsef1256.dacobot.game.explosion.data.InventoryEntity;
import net.qsef1256.dacobot.game.explosion.data.ItemEntity;
import net.qsef1256.dacobot.game.explosion.data.ItemTypeEntity;
import net.qsef1256.dacobot.system.account.data.AccountEntity;
import net.qsef1256.dacobot.system.account.model.AccountManager;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class Inventory {

    protected static final DaoCommon<Long, AccountEntity> dao = new DaoCommonImpl<>(AccountEntity.class);

    @Getter
    private final InventoryEntity data;

    public Inventory(long discord_id) {
        AccountEntity account = AccountManager.getAccount(discord_id);

        InventoryEntity inventory = account.getInventory();
        if (inventory == null) {
            inventory = new InventoryEntity().setDiscordUser(account);

            account.setInventory(inventory);
            dao.update(account);
        }
        data = inventory;
    }

    @Contract("_ -> new")
    public static @NotNull Inventory fromUser(long discord_id) {
        return new Inventory(discord_id);
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
        data.putItem(item);
    }

    public void addItem(int itemId) {
        addItem(itemId, 1);
    }

    public void addItem(int itemId, int amount) {
        ItemEntity userItem = data.getItem(itemId);
        Item item = Item.fromId(itemId, amount);

        if (userItem == null) {
            createItem(itemId, amount);
            return;
        }
        int maxAmount = item.getMaxAmount();
        int stock = userItem.getAmount();
        int result = stock + amount;
        if (result > maxAmount)
            throw new IllegalArgumentException("%s 아이템을 더 보유할 수 없습니다. 현재: %s, 최대: %s".formatted(item.getName(), result, maxAmount));

        userItem.setAmount(result);
        data.putItem(userItem);
        update();
    }

    private void createItem(int itemId) {
        createItem(itemId, 1);
    }

    private void createItem(int itemId, int amount) {
        ItemEntity itemEntity = Item.fromId(itemId, amount).getItemEntity();
        data.putItem(itemEntity);
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
            data.putItem(userItem);
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
        AccountEntity account = data.getDiscordUser();

        account.setInventory(data);
        dao.update(account);
    }

}
