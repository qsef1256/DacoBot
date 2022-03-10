package net.qsef1256.dacobot.game.explosion.model;

import lombok.Getter;
import net.qsef1256.dacobot.database.DaoCommonJpa;
import net.qsef1256.dacobot.database.DaoCommonJpaImpl;
import net.qsef1256.dacobot.database.JPAManager;
import net.qsef1256.dacobot.game.explosion.data.InventoryEntity;
import net.qsef1256.dacobot.game.explosion.data.ItemEntity;
import net.qsef1256.dacobot.game.explosion.data.ItemTypeEntity;
import net.qsef1256.dacobot.system.account.data.AccountEntity;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static net.qsef1256.dacobot.DacoBot.logger;

public class Inventory {

    protected static final DaoCommonJpa<AccountEntity, Long> dao = new DaoCommonJpaImpl<>(AccountEntity.class);

    @Getter
    private InventoryEntity data;

    public Inventory(long discordId) {
        init(discordId);
    }

    @Contract("_ -> new")
    public static @NotNull Inventory fromUser(long discordId) {
        return new Inventory(discordId);
    }

    public void init(long discordId) {
        dao.open();
        AccountEntity account = (AccountEntity) JPAManager.getEntityManager()
                .createQuery("select m from AccountEntity m join fetch m.inventory where m.discord_id = :discordId")
                .setParameter("discordId", discordId)
                .getSingleResult();

        InventoryEntity inventory = account.getInventory();
        if (inventory == null) {
            inventory = new InventoryEntity().setDiscordUser(account);

            account.setInventory(inventory);
            dao.save(account);
        }
        data = inventory;
        dao.commit();
    }

    public AccountEntity getUser() {
        return data.getDiscordUser();
    }

    public Long getUserId() {
        return getUser().getDiscord_id();
    }

    public Map<Integer, ItemEntity> getItems() {
        logger.info(dao.toString());
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
        saveAndClose();
    }

    private void createItem(int itemId) {
        createItem(itemId, 1);
    }

    private void createItem(int itemId, int amount) {
        ItemEntity itemEntity = Item.fromId(itemId, amount).getItemEntity();
        data.putItem(itemEntity);
        saveAndClose();
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
            saveAndClose();
        }
    }

    public void clearItem(int itemId) {
        Item userItem = Item.fromUser(getUserId(), itemId);

        data.removeItem(userItem.getItemEntity());
        saveAndClose();
    }

    private ItemTypeEntity getItemType(Integer itemId) {
        return Item.fromId(itemId).getItemType();
    }

    private void saveAndClose() {
        AccountEntity account = data.getDiscordUser();

        account.setInventory(data);
        dao.saveAndClose(account);
    }

}
