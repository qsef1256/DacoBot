package net.qsef1256.dacobot.game.explosion.domain.itemtype;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * DTO for {@link net.qsef1256.dacobot.game.explosion.domain.itemtype.ItemTypeEntity}
 */
public record ItemTypeDto(int itemId,
                          @NotNull String itemName,
                          @Nullable String itemIcon,
                          @Nullable String description,
                          @NotNull ItemRank itemRank,
                          int maxAmount) implements Serializable {

}