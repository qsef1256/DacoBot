package net.qsef1256.diabot.data;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "explosion_cash")
public class ExplosionCash {
    @Id
    @Column(name = "discord_id", nullable = false)
    private Long id;

    @Column(name = "cash", nullable = false)
    private Long cash;

    @Column(name = "pickaxe_count")
    private Integer pickaxeCount;

}