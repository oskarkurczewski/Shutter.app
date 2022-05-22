package pl.lodz.p.it.ssbd2022.ssbd02.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd02.util.ManagedEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Encja reprezentująca stare hasło użytkownika
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "old_password")
public class OldPassword extends ManagedEntity {

    @Version
    @Column(name = "version")
    private Long version;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(updatable = false)
    private Long id;

    /**
     * Stare hasło danego użytkownika
     */
    @NotNull
    @Column(updatable = false, unique = true, nullable = false)
    private String password;

    /**
     * Konto użytkownika, który był posiadaczem tego hasła
     *
     * @see Account
     */
    @NotNull
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false, updatable = false)
    private Account account;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OldPassword that = (OldPassword) o;
        return Objects.equals(id, that.id) && Objects.equals(password, that.password) && Objects.equals(account, that.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, password, account);
    }
}
