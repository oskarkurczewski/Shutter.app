package pl.lodz.p.it.ssbd2022.ssbd02.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Klasa reprezentująca dostępne specjalizacje fotografów
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "specialization")
public class Specialization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @NotNull
    private Long id;

    @Setter(value = AccessLevel.NONE)
    @Version
    @Column
    private Long version;

    @NotNull
    @Column(nullable = false, unique = true, length = 64)
    private String name;

    /**
     * Lista fotografów specjalizujących się w danej specjalizacji
     */
    @ToString.Exclude
    @ManyToMany(mappedBy = "specializationList")
    private List<Photographer> photographersList = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Specialization that = (Specialization) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}