package pl.lodz.p.it.ssbd2022.ssbd02.entity;

import lombok.*;
import org.hibernate.Hibernate;

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
public class Availability {

    @Setter(value = AccessLevel.NONE)
    @Version
    @Column(name = "version")
    private Long version;

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn(name = "photographer_id", nullable = false)
    private Photographer photographer;

    @Column(name = "weekday")
    @Enumerated(value = EnumType.ORDINAL)
    private WeekDay day;

    @Column(name = "from")
    private LocalTime from;

    @Column(name = "to")
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
