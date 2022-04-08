package pl.lodz.p.it.ssbd2022.ssbd02.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@ToString

@Entity
@Table(name = "review_report")
public class ReviewReport {

    @Column(name = "version")
    private Long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;


    @NotNull
    @Column(name = "reviewed", nullable = false)
    private Boolean reviewed;

    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn(name = "account_id", nullable = false)
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cause_id", nullable = false)
    @NotNull
    private ReviewReportCause cause;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ReviewReport that = (ReviewReport) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
