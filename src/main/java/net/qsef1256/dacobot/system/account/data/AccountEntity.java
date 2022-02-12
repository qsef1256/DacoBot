package net.qsef1256.dacobot.system.account.data;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.qsef1256.dacobot.game.explosion.data.CashEntity;
import net.qsef1256.dacobot.game.explosion.data.InventoryEntity;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Accessors(chain = true)
@Table(name = "discord_user")
public class AccountEntity {
    @Id
    @Column(name = "discord_id", nullable = false)
    private Long discord_id;

    private LocalDateTime registerTime = LocalDateTime.now();

    @Column(nullable = false)
    @ColumnDefault(value = "'OK'")
    private String status = "OK";

    private LocalDateTime lastAttendTime;

    @Column(nullable = false)
    @ColumnDefault(value = "0")
    private Integer attendCount = 0;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "inventory_id")
    private InventoryEntity inventory;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cash_id")
    private CashEntity explosionCash;

}
