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

@Entity
@NoArgsConstructor
@Table(name = "review")
public class Review {

    @Column(name = "version")
    private Long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "active", nullable = false)
    @NotNull
    private Boolean active = true;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "photographer_id", nullable = false)
    private Photographer photographer;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "account_id", nullable = false)
    private User user;

    @NotNull
    @Column(name = "score", nullable = false)
    private Long score;

    @Column(name = "content")
    private String content;

    @NotNull
    @Column(name = "like_count", nullable = false)
    private Long likeCount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Review review = (Review) o;
        return id != null && Objects.equals(id, review.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
