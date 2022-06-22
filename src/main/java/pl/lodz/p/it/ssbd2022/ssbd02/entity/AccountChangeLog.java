package pl.lodz.p.it.ssbd2022.ssbd02.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "account_change_log")
@NamedQueries({
        @NamedQuery(name = "account_change_log.findByLogin", query = "SELECT l FROM AccountChangeLog l WHERE l.account.login = :login"),
        @NamedQuery(name = "account_change_log.findByAccount", query = "SELECT l FROM AccountChangeLog l WHERE l.account = :account")
})
public class AccountChangeLog {

    @Setter(value = AccessLevel.NONE)
    @Version
    @Column(name = "version")
    private Long version;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    @NotNull
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @NotNull
    @Column(name = "registered", nullable = false)
    private Boolean registered;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @NotNull
    @Column(name = "login", nullable = false)
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

    @NotNull
    @Column(name = "changed_at")
    private LocalDateTime changedAt;

    @NotNull
    @Column(name = "change_type")
    private ChangeType changeType;

    @OneToOne
    @JoinColumn(name = "changed_by")
    private Account changedBy;

    public AccountChangeLog(Account account, Account by, LocalDateTime at, ChangeType type) {
        this.account = account;
        this.login = account.getLogin();
        this.email = account.getEmail();
        this.name = account.getName();
        this.surname = account.getSurname();
        this.active = account.getActive();
        this.registered = account.getRegistered();
        this.failedLogInAttempts = account.getFailedLogInAttempts();
        this.lastLogIn = account.getLastLogIn();
        this.password = account.getPassword();
        this.secret = account.getSecret();
        this.changedAt = at;
        this.changedBy = by;
        this.changeType = type;
    }
}
