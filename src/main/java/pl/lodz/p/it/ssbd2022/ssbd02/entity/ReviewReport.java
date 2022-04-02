package pl.lodz.p.it.ssbd2022.ssbd02.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@ToString

@Entity
@Table(name = "ReviewReport")
public class ReviewReport {

    @Column(name = "version")
    private Long version;

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "cause", nullable = false)
    @NotNull
    private String cause;

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
