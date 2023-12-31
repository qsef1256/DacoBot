package net.qsef1256.dacobot.module.account.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.qsef1256.dacobot.game.explosion.data.CashEntity;
import net.qsef1256.dacobot.game.explosion.data.InventoryEntity;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Accessors(chain = true)
@Table(name = "discord_user")
public class UserEntity {

    @Id
    @Column(name = "discord_id", nullable = false)
    private Long discordId;

    private LocalDateTime registerTime = LocalDateTime.now();

    // TODO: this is moved to admin service...
    @Column(nullable = false)
    @ColumnDefault(value = "'OK'")
    private String status = "OK";

    // TODO: separation required (maybe)
    private LocalDateTime lastAttendTime;

    @Column(nullable = false)
    @ColumnDefault(value = "0")
    private Integer attendCount = 0;

    // TODO: move to DiaGame
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "inventory_id")
    private InventoryEntity inventory;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cash_id")
    private CashEntity explosionCash;

}
