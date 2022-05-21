package pl.lodz.p.it.ssbd2022.ssbd02.mok.service;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.TokenType;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.VerificationToken;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.*;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.facade.TokenFacade;
import pl.lodz.p.it.ssbd2022.ssbd02.util.EmailService;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;

public class VerificationTokenService {
    @Inject
    private TokenFacade tokenFacade;

    @Inject
    private EmailService emailService;

    /**
     * Tworzy żeton używany do aktywacji konta po potwierdzeniu rejestracji przez użytkownika
     *
     * @param account Obiekt klasy Account reprezentującej dane użytkownika
     */
    public void sendRegistrationToken(Account account) {
        VerificationToken registrationToken = new VerificationToken(
                LocalDateTime.now().plusDays(1),
                account,
                TokenType.REGISTRATION_CONFIRMATION
        );
        tokenFacade.persist(registrationToken);
        emailService.sendRegistrationEmail(account.getEmail(), registrationToken);

    }

    /**
     * Usuwa żeton weryfikacyjny użyty do potwierdzenia rejestracji.
     *
     * @param token Obiekt przedstawiający żeton weryfikacyjny użyty do potwierdzenia rejestracji
     * @throws AccountConfirmedException Kiedy kotno które ma zostać potwierdzone jest już potwierdzone
     * @throws InvalidTokenException     Kiedy żeton jest nieprawidłowego typu
     * @throws NoVerificationTokenFound  Kiedy żeton nie zostanie odnaleziony w bazie
     * @throws ExpiredTokenException     Kiedy żeton wygasł
     */
    public Account confirmRegistration(String token)
            throws InvalidTokenException, AccountConfirmedException, NoVerificationTokenFound, ExpiredTokenException {
        VerificationToken registrationToken = tokenFacade.find(token);

        if (LocalDateTime.now().isAfter(registrationToken.getExpiration())) {
            throw ExceptionFactory.expiredTokenException();
        }

        if (registrationToken.getTokenType() != TokenType.REGISTRATION_CONFIRMATION) {
            throw ExceptionFactory.invalidTokenException();
        }

        if (registrationToken.getTargetUser().getRegistered()) {
            tokenFacade.remove(registrationToken);
            throw ExceptionFactory.accountConfirmedException();
        }

        tokenFacade.remove(registrationToken);
        return registrationToken.getTargetUser();
    }

    /**
     * Wysyła na adres e-mail wskazanego użytkownika link zawierający żeton resetu hasła
     *
     * @param account Konto, na którego e-mail zostać ma wysłany żeton
     * @throws NoAccountFound Jeżeli konto nie istnieje w systemie lub jest niepotwierdzone/zablokowane
     */
    public void sendPasswordResetToken(Account account) throws NoAccountFound {
        if (!account.getActive() || !account.getRegistered()) {
            throw ExceptionFactory.noAccountFound();
        }

        List<VerificationToken> oldTokens = tokenFacade.findByAccountIdAndType(account, TokenType.PASSWORD_RESET);
        for (VerificationToken oldToken : oldTokens) {
            tokenFacade.remove(oldToken);
        }

        VerificationToken registrationToken = new VerificationToken(
                LocalDateTime.now().plusMinutes(20),
                account,
                TokenType.PASSWORD_RESET
        );

        tokenFacade.persist(registrationToken);
        emailService.sendPasswordResetEmail(account.getEmail(), account.getLogin(), registrationToken);
    }

    /**
     * Sprawdza oraz usuwa żeton weryfikacyjny użyty do resetu hasła.
     *
     * @param token Obiekt przedstawiający żeton weryfikacyjny użyty do potwierdzenia rejestracji
     * @throws InvalidTokenException    Kiedy żeton jest nieprawidłowego typu lub nieaktualny
     * @throws NoVerificationTokenFound Kiedy żeton nie zostanie odnaleziony w bazie
     * @throws ExpiredTokenException    Kiedy żeton wygasł
     */
    public void confirmPasswordReset(String token)
            throws InvalidTokenException, NoVerificationTokenFound, ExpiredTokenException {
        VerificationToken resetToken = tokenFacade.find(token);

        if (LocalDateTime.now().isAfter(resetToken.getExpiration())) {
            throw ExceptionFactory.expiredTokenException();
        }

        if (resetToken.getTokenType() != TokenType.PASSWORD_RESET) {
            throw ExceptionFactory.invalidTokenException();
        }

        tokenFacade.remove(resetToken);
    }
}