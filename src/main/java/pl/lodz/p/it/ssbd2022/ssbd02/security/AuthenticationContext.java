package pl.lodz.p.it.ssbd2022.ssbd02.security;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.User;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.CustomApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAuthenticatedUser;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.facade.AuthenticationFacade;

import javax.inject.Inject;
import javax.ws.rs.core.SecurityContext;

/**
 * Kontekst uwierzytelnienia. Służy do pobrania aktualnego użytkownika po loginie z kontekstu bezpieczeństwa
 */
public class AuthenticationContext {

    @Inject
    SecurityContext securityContext;

    @Inject
    AuthenticationFacade authenticationFacade;

    public User getCurrentUser() throws NoAuthenticatedUser {
        if (securityContext.getUserPrincipal() != null) {
            return authenticationFacade.findByLogin(securityContext.getUserPrincipal().getName());
        } else {
            throw CustomApplicationException.NoAuthenticatedUser();
        }
    }
}
