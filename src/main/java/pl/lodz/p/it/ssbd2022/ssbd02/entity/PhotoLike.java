package pl.lodz.p.it.ssbd2022.ssbd02.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@ToString
@Entity
@Table(name = "PhotoLike")
public class PhotoLike {

    @EmbeddedId PhotoLikeId id;

    @Column(name = "version")
    private Long version;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "photo_id", nullable = false, insertable = false, updatable = false)
    private Photo photo;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PhotoLike photoLike = (PhotoLike) o;
        return (user.equals(photoLike.getUser()) && photo.equals((photoLike.getPhoto())));
    }

    @Override
    public int hashCode() {
        return photo.hashCode() * user.hashCode();
    }
}
