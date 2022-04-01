package pl.lodz.p.it.ssbd2022.ssbd02.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString

@Entity
@Table(name = "ReviewLike")
public class ReviewLike {

    @EmbeddedId ReviewLikeId reviewLikeId;

    @Column(name = "version")
    private Long version;

    @ManyToOne
    @JoinColumn(name = "review_id", nullable = false, insertable = false, updatable = false)
    private Review  review;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ReviewLike that = (ReviewLike) o;
        return reviewLikeId != null && Objects.equals(reviewLikeId, that.reviewLikeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reviewLikeId);
    }
}
