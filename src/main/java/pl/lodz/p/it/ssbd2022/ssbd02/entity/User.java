package pl.lodz.p.it.ssbd2022.ssbd02.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Klasa reprezentująca bazowe konto użytkownika
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "account")
@NamedQuery(name = "user.findByLogin", query = "SELECT u from User u WHERE u.login = :login")
public class User {

    @Column(name = "version")
    private Long version;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    /**
     * Flaga wskazująca czy użytkownik potwierdził rejestrację
     */
    @NotNull
    @Column(name = "registered", nullable = false)
    private Boolean registered;

    /**
     * Flaga wskazująca czy dane konto nie zostało zablokowane
     */
    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @NotNull
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotNull
    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "surname", nullable = false)
    private String surname;

    @NotNull
    @Size(min = 8, max = 64)
    @Column(name = "password", nullable = false)
    @ToString.Exclude
    private String password;

    /**
     * Lista reprezentująca poziomy dostępu danego konta
     */
    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private List<AccessLevelAssignment> accessLevelAssignmentList = new LinkedList<>();

    @ToString.Exclude
    @ManyToMany(mappedBy = "likedList")
    private List<Review> likedReviews = new ArrayList<>();

    @ToString.Exclude
    @ManyToMany(mappedBy = "likes")
    private List<Photo> likedPhotos = new ArrayList<>();

    /**
     * Użytkownicy zgłoszeni z konta
     */
    @ToString.Exclude
    @OneToMany(mappedBy = "reportee")
    private List<UserReport> reportedUsers = new ArrayList<>();

    /**
     * Zgłoszenia dotyczące konta
     */
    @ToString.Exclude
    @OneToMany(mappedBy = "reported")
    private List<UserReport> ownReports = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "user")
    private List<PhotographerReport> photographerReports = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "user")
    private List<Reservation> reservations = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

