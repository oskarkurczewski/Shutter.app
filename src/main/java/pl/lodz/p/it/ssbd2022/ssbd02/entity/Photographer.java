package pl.lodz.p.it.ssbd2022.ssbd02.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor

@Entity
@Table(name = "Photographer")
public class Photographer {

    @Column(name = "version")
    private Long version;

    @Id
    @Column(name = "user_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "specialization", nullable = false)
    private String specialization;

    @Column(name = "score", nullable = false)
    private Long score;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Photographer that = (Photographer) o;
        if (user == null || that.user == null) return false;
        return user.getId() != null && Objects.equals(user.getId(), that.user.getId());
    }

    @Override
    public int hashCode() {
        return user.getId().hashCode();
    }
}
