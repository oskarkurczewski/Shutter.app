package pl.lodz.p.it.ssbd2022.ssbd02.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
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

    @Column(name = "review_id", nullable = false, insertable = false, updatable = false)
    private Long reviewId;

    @Column(name = "user_id", nullable = false, insertable = false, updatable = false)
    private Long userId;

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
