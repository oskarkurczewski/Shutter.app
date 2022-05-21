package pl.lodz.p.it.ssbd2022.ssbd02.controllers;


import io.fusionauth.jwt.domain.JWT;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BadJWTTokenException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.ExceptionFactory;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAccountFound;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.endpoint.AccountEndpoint;
import pl.lodz.p.it.ssbd2022.ssbd02.security.JWTHandler;
import pl.lodz.p.it.ssbd2022.ssbd02.security.LoginData;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStoreHandler;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.JWTHandler.getJwtFromAuthHeader;
import static pl.lodz.p.it.ssbd2022.ssbd02.security.JWTHandler.refresh;
import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.refreshToken;


/**
 * Kontroler udostępniający końcówki obsługujące uwierzytelnianie
 * Ścieżka zasobu to /api/auth
 */
@Path("auth")
public class AuthController {

    @Inject
    private IdentityStoreHandler storeHandler;

    @Inject
    private AccountEndpoint accountEndpoint;

    /**
     * Pozwala na uwierzytelnienie użytkownika weryfikując podane przez niego poświadczenia.
     * W przypadku powodzenia zwracany jest żeton JWT, w przeciwnym wypadku zwracany jest stosowny komunikat
     * oraz rejestrowana jest nieudana próba logowania dla danego użytkownika.
     * Ścieżka zasobu to /api/auth/login.
     *
     * @param data Obiekt klasy LoginData reprezentujący przesłany przez użytkownika login oraz hasło
     * @return Obiekt klasy Response zawierający JWT lub komunikat tekstowy
     * @throws BadJWTTokenException Kiedy dostarczony żeton JWT jest nieprawidłowy
     * @throws NoAccountFound       Kiedy konto, dla którego ma zostać zarejestrowana nieudana próba
     *                              logowania nie istnieje
     * @see LoginData
     */
    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(@NotNull LoginData data) throws BaseApplicationException {
        CredentialValidationResult validationResult = storeHandler.validate(data.getCredential());

        if (validationResult.getStatus() == CredentialValidationResult.Status.VALID) {
            String token = JWTHandler.generateJWT(validationResult);

            accountEndpoint.registerSuccessfulLogInAttempt(data.getLogin());
            return Response.ok().entity(token).build();
        }

        accountEndpoint.registerFailedLogInAttempt(data.getLogin());
        throw ExceptionFactory.badJWTTokenException();
    }

    /**
     * Odświeża żeton JWT
     *
     * @param context Kontekst aplikacji
     * @return odświeżony żeton JWT
     */
    @POST
    @Path("/refresh")
    @RolesAllowed(refreshToken)
    public Response refreshToken(ContainerRequestContext context) {
        JWT oldToken = getJwtFromAuthHeader(context.getHeaderString("Authorization"));

        return Response.ok().entity(refresh(oldToken)).build();
    }
}
