package pl.lodz.p.it.ssbd2022.ssbd02.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Klasa reprezentująca możliwe powody
 * zgłoszenia użytkownika
 */

@Getter
@Setter
@ToString
@Entity
@Table(name = "account_report_cause")

@NamedQueries({
        @NamedQuery(
                name = "account_report_cause.getAccountReportCause",
                query = "SELECT cause FROM AccountReportCause AS cause WHERE cause.cause = :report_cause")
})

public class AccountReportCause {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * Powód zgłoszenia użytkownika
     */
    @NotNull
    @Column(name = "cause", nullable = false, length = 128)
    private String cause;

    @Setter(value = AccessLevel.NONE)
    @Version
    @Column(name = "version")
    private Long version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AccountReportCause that = (AccountReportCause) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}