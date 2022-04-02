package pl.lodz.p.it.ssbd2022.ssbd02.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor
public class ReviewLikeId implements Serializable {
    @JoinColumn(name = "review_id", nullable = false)
    private Long reviewId;

    @JoinColumn(name = "user_id", nullable = false)
    private Long userid;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ReviewLikeId that = (ReviewLikeId) o;
        return reviewId.equals(that.getReviewId()) && userid.equals(that.getUserid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(reviewId, userid);
    }
}
