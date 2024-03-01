package net.qsef1256.dacobot.game.explosion.v2.shop;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.qsef1256.dacobot.game.explosion.v2.itemtype.ItemTypeEntity;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "shop_price")
public class ShopEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_type")
    private ItemTypeEntity itemType;

    @ColumnDefault(value = "0")
    private Long price;

}
