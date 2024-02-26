package net.qsef1256.dacobot.game.explosion.domain.inventory;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.qsef1256.dacobot.game.explosion.domain.item.ItemEntity;
import net.qsef1256.dacobot.module.account.user.UserEntity;
import org.hibernate.Hibernate;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "user_inventory")
public class InventoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discord_id")
    private UserEntity discordUser;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Map<Integer, ItemEntity> items = new HashMap<>(); // TODO: why?

    public ItemEntity getItem(int itemId) {
        return items.get(itemId);
    }

    public void putItem(@NotNull ItemEntity item) {
        items.put(item.getItemId(), item);
    }

    public void removeItem(@NotNull ItemEntity item) {
        items.remove(item.getItemId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        InventoryEntity that = (InventoryEntity) o;

        return discordUser != null && Objects.equals(discordUser, that.discordUser);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
