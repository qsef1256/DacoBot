package net.qsef1256.dacobot.module.periodictable.entity;

import net.qsef1256.dacobot.module.periodictable.enums.GenerationCause;
import net.qsef1256.dacobot.module.periodictable.enums.Phase;
import net.qsef1256.dacobot.module.periodictable.enums.Series;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * DTO for {@link Element}
 */
public record ElementDto(int number,
                         @NotNull String symbol,
                         @NotNull String name,
                         @NotNull String engName,
                         @Nullable String alias,
                         @Nullable String description,
                         int elementGroup,
                         int elementPeriod,
                         @NotNull Series elementSeries,
                         double weight,
                         double density,
                         double melting,
                         double boiling,
                         @NotNull Phase phaseOnSATP,
                         @NotNull GenerationCause generationCause) implements Serializable {

}