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
 * Klasa reprezentująca ocenę usługi danego fotografa przez użytkownika
 */

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
@Table(name = "review")
public class Review extends ManagedEntity {

    @Setter(value = AccessLevel.NONE)
    @Version
    @Column(name = "version")
    private Long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @NotNull
    private Long id;

    /**
     * W przypadku gdy opinia zawiera nieprawidłowości,
     * takie jak oszczerstwa lub wulgarne słownictwo,
     * moderator poprzez ustawienie flagi na False jest w stanie
     * ukryć ją przed innymi użytkownikami
     */
    @Column(name = "active", nullable = false)
    @NotNull
    private Boolean active = true;

    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn(name = "photographer_id", nullable = false)
    private PhotographerInfo photographer;

    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    /**
     * Ocena przydatności recenzji wystawiona przez innych użytkowników
     */
    @NotNull
    @Column(name = "score", nullable = false)
    private Long score;

    @Column(name = "content", length = 4096)
    private String content;

    @NotNull
    @Column(name = "like_count", nullable = false)
    private Long likeCount;

    @ToString.Exclude
    @ManyToMany
    @JoinTable(
            name = "review_like",
            joinColumns = {@JoinColumn(name = "review_id")},
            inverseJoinColumns = {@JoinColumn(name = "account_id")}
    )
    private List<Account> likedList = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Review review = (Review) o;
        return id != null && Objects.equals(id, review.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public void addLikeFromUser(Account account) {
        this.likedList.add(account);
    }
}
