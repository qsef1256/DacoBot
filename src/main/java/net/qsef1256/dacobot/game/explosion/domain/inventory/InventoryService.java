package net.qsef1256.dacobot.game.explosion.domain.inventory;

import lombok.extern.slf4j.Slf4j;
import net.qsef1256.dacobot.game.explosion.domain.item.Item;
import net.qsef1256.dacobot.game.explosion.domain.item.ItemEntity;
import net.qsef1256.dacobot.module.account.entity.UserEntity;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
@Service
@Transactional
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final UserService userService;

    public InventoryService(@NotNull InventoryRepository inventoryRepository,
                            @NotNull UserService userService) {
        this.inventoryRepository = inventoryRepository;
        this.userService = userService;
    }

    public Map<Integer, ItemEntity> getItems(long discordId) {
        UserEntity user = userService.getUserOrCreate(discordId);
        InventoryEntity inventory = user.getInventory();

        return inventory != null
                ? inventory.getItems()
                : new HashMap<>();
    }

    public Item getItem(long discordId, int itemId) {
        return Item.fromEntity(getItems(discordId).get(itemId));
    }

    public void setItem(long discordId, @NotNull ItemEntity item) {
        UserEntity user = userService.getUserOrCreate(discordId);
        InventoryEntity inventory = user.getInventory();
        if (inventory != null) {
            inventory.putItem(item);

            inventoryRepository.save(inventory);
        }
    }

    private void computeItem(long discordId,
                             int itemId,
                             @NotNull Consumer<ItemEntity> action) {
        UserEntity user = userService.getUserOrCreate(discordId);
        InventoryEntity inventory = user.getInventory();
        Map<Integer, ItemEntity> items = inventory.getItems();

        items.compute(itemId, (k, v) -> {
            ItemEntity item = (v == null) ? new ItemEntity() : v;
            action.accept(item);

            return item;
        });

        inventoryRepository.save(inventory);
    }

    public void addItem(long discordId, int itemId) {
        addItem(discordId, itemId, 1);
    }

    public void addItem(long discordId,
                        int itemId,
                        int amount) {
        computeItem(discordId, itemId, item -> item.addAmount(amount));
    }

    public void createItem(long discordId,
                           int itemId,
                           int amount) {
        computeItem(discordId, itemId, item -> item.setAmount(amount));
    }

    public void removeItem(long discordId, int itemId) {
        removeItem(discordId, itemId, 1);
    }

    public void removeItem(long discordId,
                           int itemId,
                           int amount) {
        computeItem(discordId, itemId, item -> {
            int remainingAmount = item.getAmount() - amount;

            item.setAmount(Math.max(remainingAmount, 0));
        });
    }

    @Transactional
    public void clearItem(long discordId, int itemId) {
        computeItem(discordId, itemId, item -> item.setAmount(0));
    }

}