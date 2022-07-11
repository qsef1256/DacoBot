package net.qsef1256.dacobot.game.explosion.model;

import jakarta.persistence.NoResultException;
import lombok.Getter;
import net.qsef1256.dacobot.database.DaoCommonJpa;
import net.qsef1256.dacobot.database.DaoCommonJpaImpl;
import net.qsef1256.dacobot.database.JpaManager;
import net.qsef1256.dacobot.game.explosion.data.InventoryEntity;
import net.qsef1256.dacobot.game.explosion.data.ItemEntity;
import net.qsef1256.dacobot.game.explosion.data.ItemTypeEntity;
import net.qsef1256.dacobot.service.account.data.AccountEntity;
import net.qsef1256.dacobot.service.account.model.AccountManager;
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
        data = new InventoryEntity();

        // FIXME: fix inventory creating bug ?
        // TODO: https://stackoverflow.com/questions/30088649/how-to-use-multiple-join-fetch-in-one-jpql-query

        AccountEntity account;
        try {
            account = (AccountEntity) JpaManager.getEntityManager()
                    .createQuery("select m from AccountEntity m join fetch m.inventory where m.discordId = :discordId")
                    .setParameter("discordId", discordId)
                    .getSingleResult();
            data.setDiscordUser(account);
        } catch (NoResultException e) {
            logger.info("creating Inventory for %s".formatted(discordId));

            account = AccountManager.getAccount(discordId);
            account.setInventory(new InventoryEntity().setDiscordUser(account));
            dao.saveAndClose(account);
            return;
        }

        try {
            data = (InventoryEntity) JpaManager.getEntityManager()
                    .createQuery("select m from InventoryEntity m join fetch m.items where m.discordUser = :discordUser")
                    .setParameter("discordUser", account)
                    .getSingleResult();
        } catch (NoResultException e) {
            logger.info("can't find item for %s".formatted(discordId));
        }

        dao.close();
    }

    public AccountEntity getUser() {
        return data.getDiscordUser();
    }

    public Long getUserId() {
        return getUser().getDiscordId();
    }

    public Map<Integer, ItemEntity> getItems() {
        return data.getItems();
    }

    public ItemEntity getItem(int itemId) {
        return data.getItem(itemId);
    }

    public void setItem(ItemEntity item) {
        dao.open();
        data.putItem(item);
        saveAndClose();
    }

    public void addItem(int itemId) {
        addItem(itemId, 1);
    }

    public void addItem(int itemId, int amount) {
        dao.open();
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

    private void createItem(int itemId, int amount) {
        dao.open();
        ItemEntity itemEntity = Item.fromId(itemId, amount).getItemEntity();
        data.putItem(itemEntity);
        saveAndClose();
    }

    public void removeItem(int itemId) {
        removeItem(itemId, 1);
    }

    public void removeItem(int itemId, int amount) {
        dao.open();
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
        dao.open();
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
