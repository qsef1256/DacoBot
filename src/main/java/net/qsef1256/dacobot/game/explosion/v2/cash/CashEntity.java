package net.qsef1256.dacobot.game.explosion.v2.cash;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "explosion_cash")
public class CashEntity {

    @Id
    private long userId;

    @Column(name = "cash", nullable = false)
    @ColumnDefault(value = "0")
    private long cash = 0;

}
