package net.qsef1256.diabot.game.explosion.data;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.qsef1256.diabot.system.account.data.AccountEntity;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

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
    private AccountEntity discord_user;

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
