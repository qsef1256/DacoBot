package net.qsef1256.dacobot.game.explosion.data;

import lombok.Getter;
import net.qsef1256.dacobot.game.explosion.enums.ItemRank;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "explosion_item_type")
public class ItemTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer itemId;

    @Column(nullable = false)
    private String itemName;

    @Column(columnDefinition = "TEXT")
    private String itemIcon;
    
    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private ItemRank itemRank;

    @Column(nullable = false)
    @ColumnDefault(value = "1")
    private Integer maxAmount;

}
