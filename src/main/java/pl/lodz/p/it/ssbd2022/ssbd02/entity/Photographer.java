package pl.lodz.p.it.ssbd2022.ssbd02.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor

@Entity
@Table(name = "photographer")
public class Photographer {

    @Column(name = "version")
    private Long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @OneToOne
    @NotNull
    @JoinColumn(name = "account_id", nullable = false)
    private User user;

//    private List<Specialization> specializationList = new ArrayList<>();

    @Column(name = "score")
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
