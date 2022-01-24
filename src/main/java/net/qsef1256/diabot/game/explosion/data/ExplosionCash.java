package net.qsef1256.diabot.game.explosion.data;

import lombok.Getter;
import lombok.Setter;
import net.qsef1256.diabot.data.DiscordUserData;
import org.hibernate.Hibernate;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "explosion_cash")
public class ExplosionCash implements Serializable {
    @Id
    @OneToOne
    @JoinColumn(name = "discord_id")
    private DiscordUserData discord_user;

    @Column(name = "cash", nullable = false)
    @ColumnDefault(value = "0")
    private Long cash;

    @Column(name = "pickaxe_count")
    @ColumnDefault(value = "0")
    private Integer pickaxeCount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ExplosionCash that = (ExplosionCash) o;
        return discord_user != null && Objects.equals(discord_user, that.discord_user);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
