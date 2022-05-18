package pl.lodz.p.it.ssbd2022.ssbd02.security;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.ExceptionFactory;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAccountFound;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAuthenticatedAccountFound;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.facade.AuthenticationFacade;

import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;


/**
 * Kontekst uwierzytelnienia. Służy do pobrania aktualnego użytkownika po loginie z kontekstu bezpieczeństwa
 */
public class AuthenticationContext {

    @Inject
    SecurityContext securityContext;

    @Inject
    AuthenticationFacade authenticationFacade;

    public Account getCurrentUsersAccount() throws NoAuthenticatedAccountFound {
        if (securityContext.getCallerPrincipal() != null) {
            try {
                return authenticationFacade.findByLogin(securityContext.getCallerPrincipal().getName());
            } catch (NoAccountFound e) {
                throw ExceptionFactory.noAuthenticatedAccountFound();
            }
        } else {
            throw ExceptionFactory.noAuthenticatedAccountFound();
        }
    }
}
