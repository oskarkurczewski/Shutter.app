package pl.lodz.p.it.ssbd2022.ssbd02.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;


/**
 * Klasa reprezentująca poziomy dostępu
 * dostępne w tabeli bazy danych
 */
@Getter
@Setter
@Entity
@Table(name = "access_level")
public class AccessLevelValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Setter(value = AccessLevel.NONE)
    @Version
    @Column
    private Long version;

    @NotNull
    @Column(name = "name", nullable = false, unique = true, length = 64)
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AccessLevelValue that = (AccessLevelValue) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}