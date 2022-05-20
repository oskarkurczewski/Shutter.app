package pl.lodz.p.it.ssbd2022.ssbd02.mok.service;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.TokenType;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.VerificationToken;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.AccountConfirmedException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.InvalidTokenTypeException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAccountFound;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.facade.TokenFacade;
import pl.lodz.p.it.ssbd2022.ssbd02.util.EmailService;

import javax.inject.Inject;
import java.time.LocalDateTime;

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
        VerificationToken registrationToken = new VerificationToken(LocalDateTime.now().plusDays(1), account, TokenType.REGISTRATION_CONFIRMATION);
        tokenFacade.persist(registrationToken);
        emailService.sendRegistrationEmail(account.getEmail(), registrationToken);

    }

    /**
     * Usuwa żeton weryfikacyjny użyty do potwierdzenia rejestracji.
     *
     * @param token Obiekt przedstawiający żeton weryfikacyjny użyty do potwierdzenia rejestracji
     */
    public Account confirmRegistration(String token) throws InvalidTokenTypeException, AccountConfirmedException {
        VerificationToken registrationToken = tokenFacade.find(token);
        if (registrationToken.getTokenType() != TokenType.REGISTRATION_CONFIRMATION) {
            throw new InvalidTokenTypeException("Wrong token type!");
        }
        if (registrationToken.getTargetUser().getRegistered()) {
            tokenFacade.remove(registrationToken);
            throw new AccountConfirmedException("This account has been already confirmed.");
        }
        tokenFacade.remove(registrationToken);
        return registrationToken.getTargetUser();
    }
}