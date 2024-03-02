package net.qsef1256.dacobot.game.explosion.domain.inventory;

import lombok.AllArgsConstructor;
import net.qsef1256.dacobot.game.explosion.domain.item.Item;
import net.qsef1256.dacobot.game.explosion.domain.item.ItemDto;
import net.qsef1256.dacobot.game.explosion.domain.item.ItemMapper;
import net.qsef1256.dacobot.game.explosion.domain.itemtype.ItemTypeMapper;
import net.qsef1256.dacobot.game.explosion.domain.itemtype.ItemTypeRepository;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<ItemDto> getItems(long discordId) {
        Inventory inventory = getInventory(discordId);

        return inventory.getItems().stream()
                .map(ItemMapper.INSTANCE::mapItemDto)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @NotNull
    public ItemDto getItem(long discordId, int itemId) {
        return getInventory(discordId)
                .getItems()
                .stream()
                .filter(item -> item.getType().getItemId().equals(itemId))
                .findAny()
                .map(ItemMapper.INSTANCE::mapItemDto)
                .orElse(new ItemDto(ItemTypeMapper.INSTANCE
                        .mapItemTypeDto(type.getReferenceById(itemId)), 0));
    }

    public void setItem(long discordId, @NotNull ItemDto item) {
        setItem(discordId, ItemMapper.INSTANCE.mapItem(item));
    }

    public void setItem(long discordId, @NotNull Item item) {
        Inventory inventory = getInventory(discordId);
        inventory.addItem(item);

        repository.save(inventory);
    }

    @NotNull
    @Contract("_, _ -> new")
    private Item newItem(int itemId, long amount) {
        if (!type.existsById(itemId))
            throw new IllegalArgumentException("unknown item id:" + itemId);

        return new Item(type.getReferenceById(itemId), amount);
    }

    @Nullable
    private Item getItemEntity(long discordId, int itemId) {
        return getInventory(discordId)
                .getItems()
                .stream()
                .filter(item -> item.getType().getItemId().equals(itemId))
                .findAny()
                .orElse(null);
    }

    public void addItem(long discordId, int itemId) {
        addItem(discordId, itemId, 1);
    }

    public void addItem(long discordId,
                        int itemId,
                        long amount) {
        Inventory inventory = getInventory(discordId);
        Item item = getItemEntity(discordId, itemId);
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
                        long amount) {
        Inventory inventory = getInventory(discordId);
        Item item = getItemEntity(discordId, itemId);
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
                           long amount) {
        Inventory inventory = getInventory(discordId);
        Item item = getItemEntity(discordId, itemId);
        if (item == null) return;

        if (item.getAmount() > amount)
            item.removeAmount(amount);
        else
            inventory.removeItem(item);
        repository.save(inventory);
    }

    public void clearItem(long discordId, int itemId) {
        Inventory inventory = getInventory(discordId);
        Item item = getItemEntity(discordId, itemId);
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
