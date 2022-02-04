package net.qsef1256.diabot.game.explosion.data;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.qsef1256.diabot.data.UserInventoryEntity;
import org.hibernate.Hibernate;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Accessors(chain = true)
@Table(name = "explosion_item")
public class ItemEntity implements Serializable {

    @ManyToOne
    private UserInventoryEntity userInventory;

    @Id
    @Column
    private Long itemID;

    @Column
    @ColumnDefault(value = "0")
    private Long amount;

    @Column
    private LocalDateTime lastGetTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ItemEntity that = (ItemEntity) o;
        return itemID != null && Objects.equals(itemID, that.itemID);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
