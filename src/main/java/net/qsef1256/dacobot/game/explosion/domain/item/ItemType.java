package net.qsef1256.dacobot.game.explosion.domain.item;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import net.qsef1256.dacobot.game.explosion.domain.itemtype.ItemRank;
import net.qsef1256.dacobot.game.explosion.domain.itemtype.ItemTypeEntity;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * DTO for {@link ItemTypeEntity}
 */
public record ItemType(@NotNull @PositiveOrZero Integer itemId,
                       @NotNull String itemName,
                       @Nullable String itemIcon,
                       @Nullable String description,
                       ItemRank itemRank,
                       @Positive Integer maxAmount) implements Serializable {

    public ItemType {
        if (itemRank == null) itemRank = ItemRank.COMMON;
    }

}