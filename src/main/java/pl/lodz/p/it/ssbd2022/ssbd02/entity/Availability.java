package pl.lodz.p.it.ssbd2022.ssbd02.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor

@Entity
@Table(name = "availability")
public class Availability {

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
        return getClass().hashCode();
    }
}
