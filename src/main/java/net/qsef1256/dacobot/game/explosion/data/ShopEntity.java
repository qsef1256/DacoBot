package net.qsef1256.dacobot.game.explosion.data;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "shop_price")
public class ShopEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_type")
    private ItemTypeEntity itemType;

    @ColumnDefault(value = "0")
    private Long price;

}
