package net.qsef1256.dacobot.game.explosion.domain.item;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import net.qsef1256.dacobot.game.explosion.domain.itemtype.ItemType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link ItemEntity}
 */
// TODO: use MapStruct
@Builder
public record Item(@NotNull @PositiveOrZero Integer id,
                   @NotNull ItemType itemType,
                   @Positive Integer amount,
                   @Nullable LocalDateTime lastGetTime) implements Serializable {

    public Item {
        if (lastGetTime == null) lastGetTime = LocalDateTime.now();
    }

    public static Item toEntity(@NotNull Item item) {
        return Item.builder()
                .id(item.id())
                .itemType(item.itemType())
                .amount(item.amount())
                .lastGetTime(item.lastGetTime())
                .build();
    }

    public static Item fromEntity(@NotNull ItemEntity itemEntity) {
        return Item.builder()
                .id(itemEntity.getId())
                .itemType(ItemType.fromEntity(itemEntity.getItemType()))
                .amount(itemEntity.getAmount())
                .lastGetTime(itemEntity.getLastGetTime())
                .build();
    }

}
