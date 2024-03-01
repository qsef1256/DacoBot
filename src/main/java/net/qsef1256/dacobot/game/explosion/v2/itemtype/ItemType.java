package net.qsef1256.dacobot.game.explosion.v2.itemtype;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * DTO for {@link ItemTypeEntity}
 */
@Builder
public record ItemType(@NotNull @PositiveOrZero Integer itemId,
                       @NotNull String itemName,
                       @Nullable String itemIcon,
                       @Nullable String description,
                       @Nullable ItemRank itemRank,
                       @Positive Integer maxAmount) implements Serializable {

    public ItemType {
        if (itemRank == null) itemRank = ItemRank.COMMON;
    }

    public static ItemType fromEntity(@NotNull ItemTypeEntity itemType) {
        return ItemType.builder()
                .itemId(itemType.getItemId())
                .itemName(itemType.getItemName())
                .itemIcon(itemType.getItemIcon())
                .description(itemType.getDescription())
                .itemRank(itemType.getItemRank())
                .maxAmount(itemType.getMaxAmount())
                .build();
    }

}