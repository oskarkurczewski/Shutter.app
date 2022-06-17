package pl.lodz.p.it.ssbd2022.ssbd02.entity;

import lombok.*;
import org.hibernate.Hibernate;
import pl.lodz.p.it.ssbd2022.ssbd02.util.ManagedEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Klasa reprezentująca rezerwację usługi fotografa
 * przez użytkownika, wskazująca okres trwania rezerwacji
 */

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "reservation")
@NamedQueries({
        @NamedQuery(name = "reservation.findInPeriod",
                query = "SELECT r FROM Reservation r WHERE r.photographer = :photographer AND r.account = :account AND :time_from < r.timeTo AND r.timeFrom < :time_to"),
})
public class Reservation extends ManagedEntity {

    @Setter(value = AccessLevel.NONE)
    @Version
    @Column(name = "version")
    private Long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn(name = "photographer_id", nullable = false)
    private PhotographerInfo photographer;

    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @NotNull
    @Column(name = "time_from", nullable = false)
    private LocalDateTime timeFrom;

    @NotNull
    @Column(name = "time_to", nullable = false)
    private LocalDateTime timeTo;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Reservation that = (Reservation) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
