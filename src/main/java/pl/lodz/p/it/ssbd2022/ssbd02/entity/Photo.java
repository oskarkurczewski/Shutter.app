package pl.lodz.p.it.ssbd2022.ssbd02.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Klasa reprezentująca zdjęcie z portfolio fotografa
 * Każde zdjęcie powinno zawierać tytuł oraz opcjonalnie opis
 */

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "photo")
public class Photo {

    @Setter(value = AccessLevel.NONE)
    @Version
    @Column(name = "version")
    private Long version;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "photographer_id", nullable = false)
    private Photographer photographer;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data", nullable = false)
    @NotNull
    private byte[] data;

    @Column(name = "title", nullable = false)
    @NotNull
    private String title;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "like_count", nullable = false)
    private Long likeCount;

    @ToString.Exclude
    @ManyToMany
    @JoinTable(
            name = "photo_like",
            joinColumns = {@JoinColumn(name = "photo_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private List<User> likes = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Photo photo = (Photo) o;
        return id != null && Objects.equals(id, photo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
