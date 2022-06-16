package pl.lodz.p.it.ssbd2022.ssbd02.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Klasa reprezentująca możliwe powody zgłoszenia recenzji
 */

@Getter
@Setter
@Entity
@Table(name = "review_report_cause")
@NamedQueries({
        @NamedQuery(name = "review_report_cause.getAll", query = "SELECT a FROM ReviewReportCause AS a"),
})
public class ReviewReportCause {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    @Column(name = "id", nullable = false)
    private Long id;

    @Setter(value = AccessLevel.NONE)
    @Version
    @Column(name = "version")
    private Long version;

    @NotNull
    @Column(name = "cause", nullable = false, length = 128)
    private String cause;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ReviewReportCause that = (ReviewReportCause) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}