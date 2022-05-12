package pl.lodz.p.it.ssbd2022.ssbd02.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;


/**
 * Encja reprezentująca token weryfikujący
 * Zależnie od typu tokenu, może on służyć np. do potwierdzenia
 * żądania zmiany hasła lub potwierdzenia rejestracji konta
 */
@Getter
@NoArgsConstructor
@Entity
@Table(name = "token")
@NamedQueries({
        @NamedQuery(name = "VerificationToken.findByTokenEquals", query = "select v from VerificationToken v where v.token = :token")
})
public class VerificationToken {

    @Version
    @Column(name = "version")
    private Long version;

    @Id
    @Column(updatable = false)
    private Long id;

    @NotNull
    @Column(updatable = false, unique = true, nullable = false)
    private String token;

    /**
     * Data ważności tokenu
     */
    @NotNull
    @Column(nullable = false, updatable = false)
    private LocalDateTime expiration;

    /**
     * Użytkownik, którego dotyczy token
     *
     * @see Account
     */
    @NotNull
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false, updatable = false)
    private Account targetUser;

    /**
     * Typ danego tokenu
     *
     * @see TokenType
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "token_type", nullable = false, updatable = false)
    private TokenType tokenType;

    public VerificationToken(LocalDateTime expiration, Account targetUser, TokenType type) {
        this.token = UUID.randomUUID().toString();
        this.tokenType = type;
        this.expiration = expiration;
        this.targetUser = targetUser;
    }
}
