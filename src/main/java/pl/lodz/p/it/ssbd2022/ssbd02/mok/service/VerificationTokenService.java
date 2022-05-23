package pl.lodz.p.it.ssbd2022.ssbd02.mok.service;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.TokenType;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.VerificationToken;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.*;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.facade.TokenFacade;
import pl.lodz.p.it.ssbd2022.ssbd02.util.ConfigLoader;
import pl.lodz.p.it.ssbd2022.ssbd02.util.EmailService;
import pl.lodz.p.it.ssbd2022.ssbd02.util.LoggingInterceptor;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.time.LocalDateTime;
import java.util.List;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.changeSomeonesPassword;

@Stateless
@Interceptors(LoggingInterceptor.class)
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class VerificationTokenService {

    @Inject
    private TokenFacade tokenFacade;
    @Inject
    private EmailService emailService;

    @Inject
    private ConfigLoader configLoader;


    /**
     * Pomocnicza funkcja do sprawdzania podstawowych warunków żetonu
     *
     * @param token     żeton do sprawdzenia
     * @param tokenType typ żetonu jako enum
     * @throws InvalidTokenException Żeton jest nieprawidłowego typu lub nieaktualny
     * @throws ExpiredTokenException Żeton wygasł
     * @see TokenType
     */
    private void checkToken(VerificationToken token, TokenType tokenType) throws ExpiredTokenException, InvalidTokenException {
        if (LocalDateTime.now().isAfter(token.getExpiration())) {
            throw ExceptionFactory.expiredTokenException();
        }

        if (token.getTokenType() != tokenType) {
            throw ExceptionFactory.invalidTokenException();
        }
    }

    /**
     * Pomocnicza funkcja do sprawdzania, czy konto jest aktywne oraz zarejestrowane
     *
     * @param account Konto do sprawdzenia
     * @throws NoAccountFound Konto o podanej nazwie nie istnieje w systemie lub jest niepotwierdzone/zablokowane
     */
    private void checkAccount(Account account) throws NoAccountFound {
        if (!account.getActive() || !account.getRegistered()) {
            throw ExceptionFactory.noAccountFound();
        }
    }

    /**
     * Pomocnicza funkcja w celu usuwania starych żetonów
     *
     * @param account   konto, dla którego żetony mają być usunięte
     * @param tokenType typ żetonu
     */
    private void removeOldToken(Account account, TokenType tokenType) throws BaseApplicationException {
        List<VerificationToken> oldTokens = tokenFacade.findByAccountIdAndType(account, tokenType);
        for (VerificationToken oldToken : oldTokens) {
            tokenFacade.remove(oldToken);
        }
    }

    /**
     * Tworzy żeton używany do aktywacji konta po potwierdzeniu rejestracji przez użytkownika
     *
     * @param account Obiekt klasy Account reprezentującej dane użytkownika
     */
    @PermitAll
    public void sendRegistrationToken(Account account) throws BaseApplicationException {
        VerificationToken registrationToken = new VerificationToken(
                LocalDateTime.now().plusHours(configLoader.getRegistrationConfirmationTokenLifetime()),
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
    @PermitAll
    public Account confirmRegistration(String token)
            throws BaseApplicationException {
        VerificationToken registrationToken = tokenFacade.find(token);
        checkToken(registrationToken, TokenType.REGISTRATION_CONFIRMATION);

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
     * @throws NoAccountFound Konto o podanej nazwie nie istnieje w systemie lub jest niepotwierdzone/zablokowane
     */
    @PermitAll
    public void sendPasswordResetToken(Account account) throws BaseApplicationException {
        checkAccount(account);
        removeOldToken(account, TokenType.PASSWORD_RESET);

        VerificationToken verificationToken = new VerificationToken(
                LocalDateTime.now().plusMinutes(configLoader.getPasswordResetTokenLifetime()),
                account,
                TokenType.PASSWORD_RESET
        );
        tokenFacade.persist(verificationToken);

        emailService.sendPasswordResetEmail(account.getEmail(), verificationToken);
    }

    /**
     * Wysyła na adres e-mail wskazanego użytkownika link zawierający żeton resetu hasła, w przypadku, kiedy
     * zostanie ono zmienione przez administratora systemu
     *
     * @param account Konto, na które zostanie wysłany email z żetonem
     * @throws NoAccountFound Konto o podanej nazwie nie istnieje w systemie lub jest niepotwierdzone/zablokowane
     */
    @RolesAllowed(changeSomeonesPassword)
    public void sendForcedPasswordResetToken(Account account) throws BaseApplicationException {
        checkAccount(account);
        removeOldToken(account, TokenType.PASSWORD_RESET);

        VerificationToken verificationToken = new VerificationToken(
                LocalDateTime.now().plusHours(configLoader.getForcedPasswordResetTokenLifetime()),
                account,
                TokenType.PASSWORD_RESET
        );
        tokenFacade.persist(verificationToken);

        emailService.sendForcedPasswordResetEmail(account.getEmail(), verificationToken);
    }

    /**
     * Sprawdza oraz usuwa żeton weryfikacyjny użyty do resetu hasła.
     *
     * @param token Obiekt przedstawiający żeton weryfikacyjny użyty do potwierdzenia rejestracji
     * @throws InvalidTokenException    Żeton jest nieprawidłowego typu lub nieaktualny
     * @throws NoVerificationTokenFound Żeton nie zostanie odnaleziony w bazie
     * @throws ExpiredTokenException    Żeton wygasł
     */
    @PermitAll
    public Account confirmPasswordReset(String token)
            throws BaseApplicationException {
        VerificationToken resetToken = tokenFacade.find(token);
        checkToken(resetToken, TokenType.PASSWORD_RESET);
        tokenFacade.remove(resetToken);
        return resetToken.getTargetUser();
    }

    /**
     * Wysyła link zawierający żeton zmiany adresu email
     *
     * @param account Konto, na które zostanie wysłany email z żetonem
     * @throws NoAccountFound Konto o podanej nazwie nie istnieje w systemie lub jest niepotwierdzone/zablokowane
     */
    @PermitAll
    public void sendEmailUpdateToken(Account account) throws BaseApplicationException {
        checkAccount(account);
        removeOldToken(account, TokenType.EMAIL_UPDATE);

        VerificationToken verificationToken = new VerificationToken(
                LocalDateTime.now().plusHours(configLoader.getEmailResetTokenLifetime()),
                account,
                TokenType.EMAIL_UPDATE
        );
        tokenFacade.persist(verificationToken);

        emailService.sendEmailUpdateEmail(account.getEmail(), verificationToken);
    }

    @PermitAll
    public void sendUnblockOwnAccountEmail(Account account) throws BaseApplicationException {
        if (!account.getRegistered()) {
            throw ExceptionFactory.noAccountFound();
        }

        VerificationToken verificationToken = new VerificationToken(
                LocalDateTime.now().plusHours(configLoader.getUnblockOwnAccountTokenLifetime()),
                account,
                TokenType.UNBLOCK_OWN_ACCOUNT
        );

        tokenFacade.persist(verificationToken);

        emailService.sendEmailUnblockAccount(account.getEmail(), verificationToken);
    }

    /**
     * Sprawdza oraz usuwa żeton weryfikacyjny użyty do aktualizacji adresu email
     *
     * @param token Obiekt przedstawiający żeton weryfikacyjny użyty do potwierdzenia rejestracji
     * @throws InvalidTokenException    Żeton jest nieprawidłowego typu lub nieaktualny
     * @throws NoVerificationTokenFound Żeton nie zostanie odnaleziony w bazie
     * @throws ExpiredTokenException    Żeton wygasł
     */
    @PermitAll
    public Account confirmEmailUpdate(String token)
            throws BaseApplicationException {
        VerificationToken resetToken = tokenFacade.find(token);
        checkToken(resetToken, TokenType.EMAIL_UPDATE);
        tokenFacade.remove(resetToken);
        return resetToken.getTargetUser();
    }
}