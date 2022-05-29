package pl.lodz.p.it.ssbd2022.ssbd02.entity;

import lombok.*;
import org.hibernate.Hibernate;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.converter.LocaleConverter;
import pl.lodz.p.it.ssbd2022.ssbd02.util.ManagedEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Klasa reprezentująca bazowe konto użytkownika
 */
@Getter
@Setter
@NoArgsConstructor
@ToString

@Entity
@Table(name = "account")

@NamedQueries({
        @NamedQuery(name = "account.findByLogin", query = "SELECT u from Account u WHERE u.login = :login"),
        @NamedQuery(name = "account.getAccessLevelValue", query = "SELECT level FROM AccessLevelValue AS level WHERE level.name = :access_level"),
        @NamedQuery(name = "account.findByLastLogInIsBefore", query = "SELECT a from Account AS a where a.lastLogIn < :lastLogIn")
})
public class Account extends ManagedEntity {

    @Setter(value = AccessLevel.NONE)
    @Version
    @Column(name = "version")
    private Long version;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
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
    @Column(name = "locale", nullable = false)
    @Convert(converter = LocaleConverter.class)
    private Locale locale;

    @NotNull
    @Size(min = 8, max = 64)
    @Column(name = "password", nullable = false)
    @ToString.Exclude
    private String password;

    @Size(max = 36)
    @Column(name = "secret", nullable = false)
    @ToString.Exclude
    private String secret;

    @NotNull
    @Column(name = "failed_logins", nullable = false)
    @ToString.Exclude
    private Integer failedLogInAttempts;

    @Column(name = "last_login", nullable = false)
    private LocalDateTime lastLogIn;

    /**
     * Lista reprezentująca poziomy dostępu danego konta
     */
    @OneToMany(mappedBy = "account", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @ToString.Exclude
    private List<AccessLevelAssignment> accessLevelAssignmentList = new LinkedList<>();

    @ToString.Exclude
    @ManyToMany(mappedBy = "likedList")
    private List<Review> likedReviewsList = new ArrayList<>();

    @ToString.Exclude
    @ManyToMany(mappedBy = "likesList")
    private List<Photo> likedPhotosList = new ArrayList<>();

    /**
     * Użytkownicy zgłoszeni z konta
     */
    @ToString.Exclude
    @OneToMany(mappedBy = "reportee")
    private List<AccountReport> accountReportsList = new ArrayList<>();

    /**
     * Zgłoszenia dotyczące konta
     */
    @ToString.Exclude
    @OneToMany(mappedBy = "reported")
    private List<AccountReport> ownReportsList = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "account")
    private List<PhotographerReport> photographerReportsList = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "account")
    private List<Reservation> reservationsList = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "account")
    private List<OldPassword> oldPasswordList = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Account account = (Account) o;
        return id != null && Objects.equals(id, account.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

