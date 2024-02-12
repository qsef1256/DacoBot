package net.qsef1256.dacobot.game.explosion.domain.inventory;

import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.qsef1256.dacobot.game.explosion.domain.item.Item;
import net.qsef1256.dacobot.game.explosion.domain.item.ItemEntity;
import net.qsef1256.dacobot.game.explosion.domain.itemtype.ItemTypeEntity;
import net.qsef1256.dacobot.game.explosion.v2.cash.CashService;
import net.qsef1256.dacobot.module.account.entity.UserEntity;
import net.qsef1256.dacobot.setting.constants.DiaColor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
@Service
@Transactional
// FIXME: is transactional work properly? https://cheese10yun.github.io/spring-transacion-same-bean/
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final UserService userService;
    private final CashService cashService;

    public InventoryService(@NotNull InventoryRepository inventoryRepository,
                            @NotNull UserService userService,
                            @NotNull CashService cashService) {
        this.inventoryRepository = inventoryRepository;
        this.userService = userService;
        this.cashService = cashService;
    }

    // TODO: move it to domain layer? or leave it service layer because it is business logic?
    @NotNull
    public EmbedBuilder getInventoryEmbed(@NotNull User user) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setAuthor(user.getName(), null, user.getEffectiveAvatarUrl())
                .setColor(DiaColor.INFO)
                .setTitle("%s의 인벤토리".formatted(user.getName()));

        StringBuilder items = new StringBuilder();
        getItems(user.getIdLong()).forEach((id, item) -> {
            ItemTypeEntity itemType = item.getItemType();

            String itemInfo = "%s %s : %s > %s개".formatted(
                    itemType.getItemIcon(),
                    itemType.getItemName(),
                    itemType.getItemRank(),
                    item.getAmount());
            items.append(itemInfo);
            items.append("\n");
        });

        embedBuilder.addField("아이템 목록",
                items.toString(), false);
        embedBuilder.addField(":moneybag:돈",
                cashService.getCash(user.getIdLong()) + " 캐시", true);
        embedBuilder.addField(":gem:보유 다이아",
                cashService.getPickaxeCount(user.getIdLong()) + " 개", true);

        return embedBuilder;
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

    public void clearItem(long discordId, int itemId) {
        computeItem(discordId, itemId, item -> item.setAmount(0));
    }

}
