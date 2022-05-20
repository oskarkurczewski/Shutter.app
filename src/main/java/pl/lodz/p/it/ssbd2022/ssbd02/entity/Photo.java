package pl.lodz.p.it.ssbd2022.ssbd02.entity;

import lombok.*;
import org.hibernate.Hibernate;
import pl.lodz.p.it.ssbd2022.ssbd02.util.ManagedEntity;

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
public class Photo extends ManagedEntity {

    @Setter(value = AccessLevel.NONE)
    @Version
    @Column(name = "version")
    private Long version;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "photographer_id", nullable = false)
    private PhotographerInfo photographer;

    @Id
    @Column(name = "id", nullable = false)
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data", nullable = false)
    @NotNull
    private byte[] data;

    @Column(name = "title", nullable = false, length = 64)
    @NotNull
    private String title;

    @Column(name = "description", length = 1024)
    private String description;

    @NotNull
    @Column(name = "like_count", nullable = false)
    private Long likeCount;

    @ToString.Exclude
    @ManyToMany
    @JoinTable(
            name = "photo_like",
            joinColumns = {@JoinColumn(name = "photo_id")},
            inverseJoinColumns = {@JoinColumn(name = "account_id")}
    )
    private List<Account> likesList = new ArrayList<>();

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
