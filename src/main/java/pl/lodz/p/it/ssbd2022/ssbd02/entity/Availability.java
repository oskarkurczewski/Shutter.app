package pl.lodz.p.it.ssbd2022.ssbd02.entity;

import lombok.*;
import org.hibernate.Hibernate;
import pl.lodz.p.it.ssbd2022.ssbd02.util.ManagedEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Klasa reprezentująca godziny dostępności
 * wybranego fotografa w danym dniu tygodnia
 */

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "availability")
@NamedQueries({
        @NamedQuery(name = "availability.findInPeriod",
                query = "SELECT a FROM Availability a WHERE a.photographer = :photographer AND a.day = :day AND a.from < :from AND :to < a.to"),
})
public class Availability extends ManagedEntity {

    @Setter(value = AccessLevel.NONE)
    @Version
    @Column(name = "version")
    private Long version;

    @Id
    @Column(name = "id", nullable = false)
    @NotNull
    private Long id;

    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn(name = "photographer_id", nullable = false)
    private PhotographerInfo photographer;

    @Column(name = "weekday")
    @NotNull
    @Enumerated(value = EnumType.ORDINAL)
    private WeekDay day;

    @Column(name = "from")
    @NotNull
    private LocalTime from;

    @Column(name = "to")
    @NotNull
    private LocalTime to;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Availability that = (Availability) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
