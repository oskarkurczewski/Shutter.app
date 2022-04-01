package pl.lodz.p.it.ssbd2022.ssbd02.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor
class PhotoLikeId implements Serializable {
    @NotNull
    @JoinColumn(name = "photo_id", nullable = false)
    Long photoId;
    @NotNull
    @JoinColumn(name = "user_id", nullable = false)
    Long userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PhotoLikeId that = (PhotoLikeId) o;

        return photoId.equals(that.photoId) && userId.equals(that.getUserId());
    }

    @Override
    public int hashCode() {
        int result = photoId != null ? photoId.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }
}
