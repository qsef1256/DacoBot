package net.qsef1256.dacobot.game.explosion.data;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.qsef1256.dacobot.module.account.entity.UserEntity;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Accessors(chain = true)
@Table(name = "explosion_cash")
public class CashEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cashId;

    @OneToOne(mappedBy = "explosionCash")
    private UserEntity discordUser;

    @Column(name = "cash", nullable = false)
    @ColumnDefault(value = "0")
    private Long cash = 0L;

    @Column(name = "pickaxe_count")
    @ColumnDefault(value = "0")
    private Integer pickaxeCount = 0;

    @Column
    @ColumnDefault(value = "0")
    private Integer prestigeCount = 0;

}
