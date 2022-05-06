package pl.lodz.p.it.ssbd2022.ssbd02.security;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.CustomApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAuthenticatedUserFound;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAccountFound;
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

    public Account getCurrentUsersAccount() throws NoAuthenticatedUserFound {
        if (securityContext.getCallerPrincipal() != null) {
            try {
                return authenticationFacade.findByLogin(securityContext.getCallerPrincipal().getName());
            } catch (NoAccountFound e) {
                throw CustomApplicationException.noAuthenticatedUserFound();
            }
        } else {
            throw CustomApplicationException.noAuthenticatedUserFound();
        }
    }
}
