package net.qsef1256.dacobot.game.explosion.domain.inventory;

import lombok.extern.slf4j.Slf4j;
import net.qsef1256.dacobot.game.explosion.domain.item.ItemEntity;
import net.qsef1256.dacobot.game.explosion.domain.item.ItemOld;
import net.qsef1256.dacobot.game.explosion.domain.item.ItemService;
import net.qsef1256.dacobot.module.account.controller.AccountController;
import net.qsef1256.dacobot.module.account.entity.UserEntity;
import net.qsef1256.dacobot.module.account.entity.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final UserRepository userRepository;
    private final AccountController accountController;
    private final ItemService itemService;

    @Autowired
    public InventoryService(@NotNull InventoryRepository inventoryRepository,
                            @NotNull UserRepository userRepository,
                            @NotNull AccountController accountController,
                            @NotNull ItemService itemService) {
        this.inventoryRepository = inventoryRepository;
        this.userRepository = userRepository;
        this.accountController = accountController;
        this.itemService = itemService;
    }

    public InventoryEntity getInventory(long discordId) {
        Optional<UserEntity> optionalUser = userRepository.findByDiscordId(discordId);
        UserEntity account = optionalUser.orElseGet(() -> createAccount(discordId));

        return inventoryRepository.findByDiscordUser(account)
                .orElseGet(() -> createInventory(account));
    }

    public UserEntity getUser(long discordId) {
        return userRepository.findByDiscordId(discordId).orElse(null);
    }

    public Long getUserId(long discordId) {
        return getUser(discordId).getDiscordId();
    }

    public Map<Integer, ItemEntity> getItems(long discordId) {
        return getInventory(discordId).getItems();
    }

    public ItemEntity getItem(long discordId, int itemId) {
        return getInventory(discordId).getItem(itemId);
    }

    public void setItem(long discordId, ItemEntity item) {
        InventoryEntity inventory = getInventory(discordId);
        inventory.putItem(item);
        saveInventory(inventory);
    }

    public void addItem(long discordId, int itemId) {
        addItem(discordId, itemId, 1);
    }

    public void addItem(long discordId,
                        int itemId,
                        int amount) {
        ItemEntity userItem = getItem(discordId, itemId);
        ItemOld item = ItemOld.fromId(itemId, amount);
        if (userItem == null) {
            createItem(discordId, itemId, amount);
            return;
        }

        int maxAmount = item.getMaxAmount();
        int stock = userItem.getAmount();
        int result = stock + amount;
        if (result > maxAmount)
            throw new IllegalArgumentException("%s 아이템을 더 보유할 수 없습니다. 현재: %s, 최대: %s".formatted(
                    item.getName(), result, maxAmount));

        userItem.setAmount(result);
        setItem(discordId, userItem);
    }

    private void createItem(long discordId,
                            int itemId,
                            int amount) {
        ItemEntity itemEntity = ItemOld.fromId(itemId, amount).getItemEntity();

        setItem(discordId, itemEntity);
    }

    public void removeItem(long discordId, int itemId) {
        removeItem(discordId, itemId, 1);
    }

    public void removeItem(long discordId,
                           int itemId,
                           int amount) {
        ItemEntity userItem = getItem(discordId, itemId);
        InventoryEntity inventory = getInventory(discordId);

        ItemOld item = ItemOld.fromId(itemId, amount);

        int stock = userItem.getAmount();
        int result = stock - amount;
        if (result < 0)
            throw new IllegalArgumentException("%s 아이템을 더 삭제할 수 없습니다. 재고량: %s".formatted(item.getName(), stock));
        if (result == 0) clearItem(discordId, itemId);
        else {
            userItem.setAmount(result);
            setItem(discordId, userItem);
        }
    }

    public void clearItem(long userId, int itemId) {
        ItemOld userItem = ItemOld.fromUser(getUserId(userId), itemId);

        removeItem(userId,
                userItem.getItemEntity().getItemId(),
                userItem.getAmount());
    }

    @NotNull
    private UserEntity createAccount(long discordId) {
        log.info("Creating user account for {}", discordId);
        UserEntity userEntity = accountController.getAccount(discordId);
        userRepository.save(userEntity);

        return userEntity;
    }

    private InventoryEntity createInventory(@NotNull UserEntity account) {
        log.info("Creating inventory for {}", account.getDiscordId());
        InventoryEntity inventoryEntity = new InventoryEntity().setDiscordUser(account);
        inventoryRepository.save(inventoryEntity);

        return inventoryEntity;
    }

    private void saveInventory(@NotNull InventoryEntity inventoryEntity) {
        inventoryRepository.save(inventoryEntity);
    }

}
