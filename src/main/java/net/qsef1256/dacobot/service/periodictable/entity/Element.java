package net.qsef1256.dacobot.service.periodictable.entity;

import jakarta.persistence.*;
import lombok.*;
import net.qsef1256.dacobot.service.periodictable.enums.GenerationCause;
import net.qsef1256.dacobot.service.periodictable.enums.Phase;
import net.qsef1256.dacobot.service.periodictable.enums.Series;
import org.hibernate.Hibernate;

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
    private String symbol;

    private String name;
    private String engName;
    private String desc;

    private int group;
    private int period;
    @Enumerated(EnumType.STRING)
    private Series series;

    private double weight;
    private double density;
    private double melting;
    private double boiling;
    @Enumerated(EnumType.STRING)
    private Phase phaseOnSATP;
    @Enumerated(EnumType.STRING)
    private GenerationCause generationCause;

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
