package net.qsef1256.dacobot.game.explosion.domain.inventory;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.qsef1256.dacobot.game.explosion.domain.itemtype.ItemTypeEntity;
import net.qsef1256.dacobot.module.account.entity.UserEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "new_user_inventory")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewInventoryEntity {

    @Id
    public long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discord_id")
    private UserEntity discordUser;
    @OneToOne
    @JoinColumn(name = "item_type")
    private ItemTypeEntity itemType;
    @Positive
    private long amount = 1;

    private LocalDateTime lastGetTime;

}
