package net.qsef1256.dacobot.game.explosion.v2.inventory;

import lombok.AllArgsConstructor;
import net.qsef1256.dacobot.game.explosion.v2.item.Item;
import net.qsef1256.dacobot.game.explosion.v2.itemtype.ItemTypeRepository;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class InventoryService {

    private final ItemTypeRepository type;
    private final InventoryRepository repository;

    @NotNull
    private Inventory getInventory(long discordId) {
        if (!repository.existsById(discordId)) repository.save(new Inventory(discordId));

        return repository.getReferenceById(discordId);
    }

    @NotNull
    public List<Item> getItems(long discordId) {
        Inventory inventory = getInventory(discordId);

        return inventory.getItems();
    }

    @Nullable
    public Item getItem(long discordId, int itemId) {
        Inventory inventory = getInventory(discordId);

        return inventory.getItemByTypeId(itemId);
    }

    public void setItem(long discordId, @NotNull Item item) {
        Inventory inventory = getInventory(discordId);
        inventory.addItem(item);

        repository.save(inventory);
    }

    @NotNull
    @Contract("_, _ -> new")
    private Item newItem(int itemId, int amount) {
        if (!type.existsById(itemId))
            throw new IllegalArgumentException("unknown item id:" + itemId);

        return new Item(type.getReferenceById(itemId), amount);
    }

    public void addItem(long discordId, int itemId) {
        addItem(discordId, itemId, 1);
    }

    public void addItem(long discordId,
                        int itemId,
                        int amount) {
        Inventory inventory = getInventory(discordId);
        Item item = inventory.getItemByTypeId(itemId);
        if (item != null)
            item.addAmount(amount);
        else
            inventory.addItem(newItem(itemId, amount));

        repository.save(inventory);
    }

    public void setItem(long discordId, int itemId) {
        setItem(discordId, itemId, 1);
    }

    public void setItem(long discordId,
                        int itemId,
                        int amount) {
        Inventory inventory = getInventory(discordId);
        Item item = inventory.getItemByTypeId(itemId);
        if (item == null)
            inventory.addItem(newItem(itemId, amount));
        else
            item.setAmount(amount);

        repository.save(inventory);
    }

    public void removeItem(long discordId, int itemId) {
        removeItem(discordId, itemId, 1);
    }

    public void removeItem(long discordId,
                           int itemId,
                           int amount) {
        Inventory inventory = getInventory(discordId);
        Item item = inventory.getItemByTypeId(itemId);
        if (item == null) return;

        if (item.getAmount() > amount)
            item.removeAmount(amount);
        else
            inventory.removeItem(item);
        repository.save(inventory);
    }

    public void clearItem(long discordId, int itemId) {
        Inventory inventory = getInventory(discordId);
        Item item = inventory.getItemByTypeId(itemId);
        if (item == null) return;

        inventory.removeItem(item);
        repository.save(inventory);
    }

    public void clearInventory(long discordId) {
        Inventory inventory = getInventory(discordId);

        inventory.getItems().clear();
        repository.save(inventory);
    }

}
