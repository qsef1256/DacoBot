package net.qsef1256.dacobot.game.explosion.v2.item;

import jakarta.validation.constraints.PositiveOrZero;
import net.qsef1256.dacobot.game.explosion.v2.itemtype.ItemTypeDto;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link Item}
 */
public record ItemDto(@NotNull ItemTypeDto type,
                      @PositiveOrZero long amount) implements Serializable {

}