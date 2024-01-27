package net.qsef1256.dacobot.game.explosion.domain.item;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link ItemEntity}
 */
@Builder
public record Item(@NotNull @PositiveOrZero Integer id,
                   @NotNull ItemType itemType,
                   @Positive Integer amount,
                   LocalDateTime lastGetTime) implements Serializable {

    public Item {
        if (lastGetTime == null) lastGetTime = LocalDateTime.now();
    }

}