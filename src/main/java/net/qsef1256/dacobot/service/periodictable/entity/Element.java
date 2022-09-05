package net.qsef1256.dacobot.service.periodictable.entity;

import jakarta.persistence.*;
import lombok.*;
import net.qsef1256.dacobot.service.periodictable.enums.GenerationCause;
import net.qsef1256.dacobot.service.periodictable.enums.Phase;
import net.qsef1256.dacobot.service.periodictable.enums.Series;
import org.hibernate.Hibernate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Table(name = "element_data")
public class Element {

    @Id
    private int number;
    private @NotNull String symbol;

    private @NotNull String name;
    private @NotNull String engName;
    private @Nullable String alias;

    private @Nullable String description;

    private int elementGroup;
    private int elementPeriod;
    @Enumerated(EnumType.STRING)
    private @NotNull Series elementSeries;

    private double weight;
    private double density;
    private double melting;
    private double boiling;
    @Enumerated(EnumType.STRING)
    private @NotNull Phase phaseOnSATP;
    @Enumerated(EnumType.STRING)
    private @NotNull GenerationCause generationCause;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Element element = (Element) o;
        return Objects.equals(number, element.number);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
