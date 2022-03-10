package net.qsef1256.dacobot.game.explosion.data;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    private ItemTypeEntity itemType;

    @Column
    @ColumnDefault(value = "1")
    private Integer amount = 1;

    @Column
    private LocalDateTime lastGetTime = LocalDateTime.now();

    public ItemEntity(ItemTypeEntity itemType) {
        this();
        setItemType(itemType);
    }

    public ItemEntity(ItemTypeEntity itemType, int amount) {
        this(itemType);
        setAmount(amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ItemEntity that = (ItemEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public int getItemId() {
        return getItemType().getItemId();
    }
}
