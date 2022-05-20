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

    private static final int TOKEN_TIME = 20;


    /**
     * Pomocnicza funkcja do sprawdzania podstawowych warunków żetonu
     *
     * @param token     żeton do sprawdzenia
     * @param tokenType typ żetonu jako enum
     * @return zwraca żeton w celu dalszego sprawdzania warunków
     * @throws InvalidTokenException    Żeton jest nieprawidłowego typu lub nieaktualny
     * @throws NoVerificationTokenFound Żeton nie zostanie odnaleziony w bazie
     * @throws ExpiredTokenException    Żeton wygasł
     * @see TokenType
     */
    private VerificationToken checkToken(String token, TokenType tokenType) throws NoVerificationTokenFound, ExpiredTokenException, InvalidTokenException {
        VerificationToken verificationToken = tokenFacade.find(token);
        if (LocalDateTime.now().isAfter(verificationToken.getExpiration())) {
            throw ExceptionFactory.expiredTokenException();
        }

        if (verificationToken.getTokenType() != tokenType) {
            throw ExceptionFactory.invalidTokenException();
        }
        return verificationToken;
    }

    /**
     * Pomocnicza funkcja do tworzenia żetonu weryfikacyjnego
     *
     * @param account   Konto, dla którego zostanie utworzony wysłany email z żetonem
     * @param tokenType typ żetonu
     * @return utworzony żeton
     * @throws NoAccountFound Konto nie istnieje w systemie lub jest niepotwierdzone/zablokowane
     * @see TokenType
     */
    private VerificationToken createNewToken(Account account, TokenType tokenType) throws NoAccountFound {
        if (!account.getActive() || !account.getRegistered()) {
            throw ExceptionFactory.noAccountFound();
        }

        List<VerificationToken> oldTokens = tokenFacade.findByAccountIdAndType(account, tokenType);
        for (VerificationToken oldToken : oldTokens) {
            tokenFacade.remove(oldToken);
        }

        VerificationToken registrationToken = new VerificationToken(
                LocalDateTime.now().plusMinutes(VerificationTokenService.TOKEN_TIME),
                account,
                tokenType
        );

        return tokenFacade.persist(registrationToken);
    }

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
     * @throws AccountConfirmedException Konto do potwierdzenia jest już potwierdzone
     * @throws InvalidTokenException     Żeton jest nieprawidłowego typu lub nieaktualny
     * @throws NoVerificationTokenFound  Żeton nie zostanie odnaleziony w bazie
     * @throws ExpiredTokenException     Żeton wygasł
     */
    public Account confirmRegistration(String token)
            throws InvalidTokenException, AccountConfirmedException, NoVerificationTokenFound, ExpiredTokenException {
        VerificationToken registrationToken = checkToken(token, TokenType.REGISTRATION_CONFIRMATION);

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
     * @param account Konto, na które zostanie wysłany email z żetonem
     * @throws NoAccountFound Konto nie istnieje w systemie lub jest niepotwierdzone/zablokowane
     */
    public void sendPasswordResetToken(Account account) throws NoAccountFound {
        VerificationToken verificationToken = createNewToken(account, TokenType.PASSWORD_RESET);
        emailService.sendPasswordResetEmail(account.getEmail(), account.getLogin(), verificationToken);
    }

    /**
     * Sprawdza oraz usuwa żeton weryfikacyjny użyty do resetu hasła.
     *
     * @param token Obiekt przedstawiający żeton weryfikacyjny użyty do potwierdzenia rejestracji
     * @throws InvalidTokenException    Żeton jest nieprawidłowego typu lub nieaktualny
     * @throws NoVerificationTokenFound Żeton nie zostanie odnaleziony w bazie
     * @throws ExpiredTokenException    Żeton wygasł
     */
    public void confirmPasswordReset(String token)
            throws InvalidTokenException, NoVerificationTokenFound, ExpiredTokenException {
        VerificationToken resetToken = checkToken(token, TokenType.PASSWORD_RESET);
        tokenFacade.remove(resetToken);
    }

    /**
     * Wysyła link zawierający żeton zmiany adresu email
     *
     * @param account Konto, na które zostanie wysłany email z żetonem
     * @throws NoAccountFound Konto nie istnieje w systemie lub jest niepotwierdzone/zablokowane
     */
    public void sendEmailUpdateToken(Account account, String newEmail) throws NoAccountFound {
        VerificationToken verificationToken = createNewToken(account, TokenType.EMAIL_UPDATE);
        emailService.sendEmailUpdateEmail(newEmail, account.getLogin(), verificationToken);
    }

    /**
     * Sprawdza oraz usuwa żeton weryfikacyjny użyty do aktualizacji hasła
     *
     * @param token Obiekt przedstawiający żeton weryfikacyjny użyty do potwierdzenia rejestracji
     * @throws InvalidTokenException    Żeton jest nieprawidłowego typu lub nieaktualny
     * @throws NoVerificationTokenFound Żeton nie zostanie odnaleziony w bazie
     * @throws ExpiredTokenException    Żeton wygasł
     */
    public void confirmEmailUpdate(String token)
            throws InvalidTokenException, NoVerificationTokenFound, ExpiredTokenException {
        VerificationToken resetToken = checkToken(token, TokenType.EMAIL_UPDATE);
        tokenFacade.remove(resetToken);
    }
}