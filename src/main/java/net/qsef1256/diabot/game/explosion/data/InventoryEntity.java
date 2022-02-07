package net.qsef1256.diabot.game.explosion.data;

import lombok.Getter;
import lombok.Setter;
import net.qsef1256.diabot.system.account.data.AccountEntity;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "user_inventory")
public class InventoryEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discord_id")
    private AccountEntity discordUser;

    @OneToMany
    private Map<Integer, ItemEntity> items;

    public ItemEntity getItem(int itemId) {
        return items.get(itemId);
    }

    public void setItem(ItemEntity item) {
        items.put(item.getItemId(), item);
    }

    public void removeItem(ItemEntity item) {
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
