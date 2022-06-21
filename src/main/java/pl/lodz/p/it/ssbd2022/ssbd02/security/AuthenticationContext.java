package pl.lodz.p.it.ssbd2022.ssbd02.security;

import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.ExceptionFactory;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAuthenticatedAccountFound;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import java.security.Principal;


/**
 * Kontekst uwierzytelnienia. Służy do pobrania aktualnego użytkownika po loginie z kontekstu bezpieczeństwa
 */
@Stateless
public class AuthenticationContext {

    @Inject
    SecurityContext securityContext;

    public String getCurrentUsersLogin() throws NoAuthenticatedAccountFound {
        return getCurrentPrincipal().getName();
    }

    private Principal getCurrentPrincipal() throws NoAuthenticatedAccountFound {
        if (securityContext.getCallerPrincipal() != null) {
            return securityContext.getCallerPrincipal();
        } else {
            throw ExceptionFactory.noAuthenticatedAccountFound();
        }
    }
}
