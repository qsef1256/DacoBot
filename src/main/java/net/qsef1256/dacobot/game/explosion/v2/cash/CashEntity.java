package net.qsef1256.dacobot.game.explosion.v2.cash;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import net.qsef1256.dacobot.module.account.entity.UserEntity;
import org.hibernate.annotations.ColumnDefault;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "new_explosion_cash")
public class CashEntity implements Serializable {

    @Id
    private Long id;

    @MapsId
    @OneToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "explosion_cash", nullable = false)
    @ColumnDefault(value = "0")
    private long cash = 0;

    @Column(name = "pickaxe_count")
    @ColumnDefault(value = "0")
    private int pickaxeCount = 0;

}
