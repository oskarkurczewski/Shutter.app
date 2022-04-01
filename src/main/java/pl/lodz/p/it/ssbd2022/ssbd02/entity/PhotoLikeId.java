package pl.lodz.p.it.ssbd2022.ssbd02.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor
class PhotoLikeId implements Serializable {
    @ManyToOne
    @JoinColumn(name = "photo_id", nullable = false)
    Photo photo;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PhotoLikeId that = (PhotoLikeId) o;

        return photo.equals(that.photo) && user.equals(that.getUser());
    }

    @Override
    public int hashCode() {
        int result = photo != null ? photo.hashCode() : 0;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }
}
