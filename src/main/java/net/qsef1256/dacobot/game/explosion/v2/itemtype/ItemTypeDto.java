package net.qsef1256.dacobot.game.explosion.v2.itemtype;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * DTO for {@link net.qsef1256.dacobot.game.explosion.v2.itemtype.ItemTypeEntity}
 */
public record ItemTypeDto(int itemId,
                          @NotNull String itemName,
                          @Nullable String itemIcon,
                          @Nullable String description,
                          @NotNull ItemRank itemRank,
                          int maxAmount) implements Serializable {

}