package net.qsef1256.diabot.data;

import lombok.Getter;
import lombok.Setter;
import net.qsef1256.diabot.game.explosion.data.ItemEntity;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "user_inventory")
public class UserInventoryEntity implements Serializable {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "discord_id")
    private DiscordUserEntity discordUser;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemEntity> items;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserInventoryEntity that = (UserInventoryEntity) o;
        return discordUser != null && Objects.equals(discordUser, that.discordUser);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
