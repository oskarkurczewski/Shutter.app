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
 * Klasa reprezentująca fotografa
 * Fotograf musi być powiązany z kontem użytkownika
 */

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "photographer_info")
@NamedQuery(
        name = "photographer_info.findByLogin",
        query = "SELECT p FROM PhotographerInfo p JOIN Account a ON p.id=a.id WHERE a.login = :login"
)
public class PhotographerInfo extends ManagedEntity {

    @Setter(value = AccessLevel.NONE)
    @Version
    @Column(name = "version")
    private Long version;

    @Id
    @Column(name = "account_id")
    @NotNull
    private Long id;

    @OneToOne
    @NotNull
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ToString.Exclude
    @OneToMany(mappedBy = "photographer", cascade = {CascadeType.REMOVE})
    private List<Photo> photos = new ArrayList<>();

    @ToString.Exclude
    @ManyToMany
    @JoinTable(
            name = "photographer_specialization",
            joinColumns = {@JoinColumn(name = "photographer_id")},
            inverseJoinColumns = {@JoinColumn(name = "specialization_id")}
    )
    private List<Specialization> specializationList = new ArrayList<>();

    @Column(name = "score")
    @NotNull
    private Long score;

    @Column(name = "review_count")
    @NotNull
    private Long reviewCount;

    @Column(name = "description", length = 4096)
    private String description;

    @Column(name = "lat")
    private Double latitude;

    @Column(name = "long")
    private Double longitude;

    @Column(name = "visible")
    private Boolean visible;

    @ToString.Exclude
    @OneToMany(mappedBy = "photographer", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Availability> availability;

    @ToString.Exclude
    @OneToMany(mappedBy = "photographer")
    private List<PhotographerReport> reportsList = new ArrayList<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "photographer")
    private List<Reservation> reservationsList = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        PhotographerInfo that = (PhotographerInfo) o;
        if (account == null || that.account == null) return false;
        return account.getId() != null && Objects.equals(account.getId(), that.account.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(account.getId());
    }
}
