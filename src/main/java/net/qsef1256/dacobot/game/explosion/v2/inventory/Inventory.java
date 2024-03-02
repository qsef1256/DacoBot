package net.qsef1256.dacobot.game.explosion.v2.inventory;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.qsef1256.dacobot.game.explosion.v2.item.Item;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "explosion_inventory")
public class Inventory {

    @Id
    private long userId;

    @ElementCollection
    @CollectionTable(
            name = "explosion_inventory_item",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "userId")
    )
    private final List<Item> items = new ArrayList<>();

    public Inventory(long userId) {
        this.userId = userId;
    }

    public void addItem(@NotNull Item item) {
        items.add(item);
    }

    public void removeItem(@NotNull Item item) {
        items.remove(item);
    }

}
