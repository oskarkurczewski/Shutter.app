package pl.lodz.p.it.ssbd2022.ssbd02.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Klasa reprezentująca zgłoszenia fotografa
 * Zgłoszenie jest tworzone przez innych użytkowników -
 * klientów danego fotografa, gdy są niezadowoleni z usług
 */

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@Table(name = "photographer_report")
public class PhotographerReport {

    @Setter(value = AccessLevel.NONE)
    @Version
    @Column(name = "version")
    private Long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    @Column(name = "id", nullable = false)
    private Long id;


    /**
     * Flaga wskazująca czy moderator rozpatrzył zgłoszenie
     */
    @NotNull
    @Column(name = "reviewed")
    private Boolean reviewed;

    /**
     * Użytkownik zgłaszający
     */
    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "account_id", nullable = false)
    private User user;

    /**
     * Zgłaszany fotograf
     */
    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn(name = "photographer_id", nullable = false)
    private Photographer photographer;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cause_id", nullable = false)
    @NotNull
    private PhotographerReportCause cause;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PhotographerReport that = (PhotographerReport) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
