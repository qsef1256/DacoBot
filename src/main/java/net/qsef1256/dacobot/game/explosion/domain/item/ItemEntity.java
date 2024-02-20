package net.qsef1256.dacobot.game.explosion.domain.item;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.qsef1256.dacobot.game.explosion.domain.itemtype.ItemTypeEntity;
import org.hibernate.Hibernate;
import org.hibernate.annotations.ColumnDefault;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Accessors(chain = true)
@Table(name = "explosion_item")
@NoArgsConstructor
public class ItemEntity {

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

    public ItemEntity(@NotNull ItemTypeEntity itemType) {
        this();

        setItemType(itemType);
    }

    public ItemEntity(@NotNull ItemTypeEntity itemType, int amount) {
        this(itemType);

        setAmount(amount);
    }

    public int getItemId() {
        return getItemType().getItemId();
    }

    public void addAmount(int amount) {
        this.amount += amount;
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

}
