package net.qsef1256.dacobot.game.explosion.v2.inventory;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.qsef1256.dacobot.game.explosion.v2.item.Item;
import net.qsef1256.dacobot.game.explosion.v2.itemtype.ItemTypeEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "user_inventory")
public class Inventory {

    @Id
    private long userId;

    @ElementCollection
    @CollectionTable(
            name = "user_inventory_item"
    )
    private final List<Item> items = new ArrayList<>();

    public Inventory(long userId) {
        this.userId = userId;
    }

    @Nullable
    public Item getItemByType(@NotNull ItemTypeEntity itemType) {
        return items.stream()
                .filter(item -> item.getType().equals(itemType))
                .findAny()
                .orElse(null);
    }

    @Nullable
    public Item getItemByTypeId(int itemTypeId) {
        return items.stream()
                .filter(item -> item.getType()
                        .getItemId()
                        .equals(itemTypeId))
                .findAny()
                .orElse(null);
    }

    public void addItem(@NotNull Item item) {
        items.add(item);
    }

    public void removeItem(@NotNull Item item) {
        items.remove(item);
    }

}
