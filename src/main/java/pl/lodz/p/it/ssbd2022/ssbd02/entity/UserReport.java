package pl.lodz.p.it.ssbd2022.ssbd02.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor

@Entity
@Table(name = "account_report")
public class UserReport {

    @Column(name = "version")
    private Long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn(name = "reported_id", nullable = false)
    private User reported;

    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn(name = "reportee_id", nullable = false)
    private User reportee;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cause_id", nullable = false)
    @NotNull
    private AccountReportCause cause;

    @NotNull
    @Column(name = "reviewed", nullable = false)
    private Boolean reviewed;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserReport that = (UserReport) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
