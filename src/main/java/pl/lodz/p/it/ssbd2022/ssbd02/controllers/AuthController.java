package pl.lodz.p.it.ssbd2022.ssbd02.controllers;


import io.fusionauth.jwt.domain.JWT;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.*;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.AuthTokenDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.endpoint.AccountEndpoint;
import pl.lodz.p.it.ssbd2022.ssbd02.security.JWTHandler;
import pl.lodz.p.it.ssbd2022.ssbd02.security.LoginData;
import pl.lodz.p.it.ssbd2022.ssbd02.security.OneTimeCodeUtils;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStoreHandler;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Groups.ADMINISTRATOR;
import static pl.lodz.p.it.ssbd2022.ssbd02.security.JWTHandler.getJwtFromAuthHeader;
import static pl.lodz.p.it.ssbd2022.ssbd02.security.JWTHandler.refresh;
import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.changeGroup;
import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.refreshToken;


/**
 * Kontroler udostępniający końcówki obsługujące uwierzytelnianie
 * Ścieżka zasobu to /api/auth
 */
@Path("auth")
public class AuthController {

    private static final Logger LOGGER = Logger.getLogger(AuthController.class.getName());

    @Inject
    private OneTimeCodeUtils oneTimeCodeUtils;
    @Context
    private HttpServletRequest httpServletRequest;
    @Context
    private SecurityContext securityContext;
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
     * @throws BadJWTTokenException Dostarczony żeton JWT jest nieprawidłowy
     * @throws NoAccountFound       Konto o podanej nazwie nie istnieje
     * @see LoginData
     */
    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(@NotNull @Valid LoginData data) throws BaseApplicationException {
        CredentialValidationResult validationResult = storeHandler.validate(data.getCredential());
        String secret = accountEndpoint.getSecret(data.getLogin());

        if (
                validationResult.getStatus() == CredentialValidationResult.Status.VALID
//                        && oneTimeCodeUtils.verifyCode(secret, data.getTwoFACode())
        ) {
            String token = JWTHandler.generateJWT(validationResult);

            if (validationResult.getCallerGroups().contains(ADMINISTRATOR)) {
                accountEndpoint.sendAdminAuthenticationWarningEmail(
                        data.getLogin(),
                        httpServletRequest.getRemoteAddr()
                );
            }

            LOGGER.log(
                    Level.INFO,
                    "Successful authentication for user {0} from IP {1}",
                    new Object[]{data.getLogin(), httpServletRequest.getRemoteAddr()}
            );

            accountEndpoint.registerSuccessfulLogInAttempt(data.getLogin());
            return Response.ok().entity(new AuthTokenDto(token)).build();
        }

        accountEndpoint.registerFailedLogInAttempt(data.getLogin());
        throw ExceptionFactory.badJWTTokenException();
    }

    /**
     * Rejestruje w dzienniku zdarzeń próbę zmiany grupy, do której przynależy użytkownik w widoku aplikacji
     *
     * @param group nazwa grupy, na którą użytkownik dokonać chciał zmiany
     * @throws UserNotInGroupException użytkownik nie należy do podanej grupy
     */
    @POST
    @Path("change-group/{group}")
    @RolesAllowed(changeGroup)
    public Response changeGroup(@PathParam("group") String group) throws UserNotInGroupException {
        if (securityContext.isUserInRole(group)) {
            LOGGER.log(Level.INFO,
                    "Successful group change to {0} for user {1} from IP {2}",
                    new Object[]{group, securityContext.getUserPrincipal().getName(),
                            httpServletRequest.getRemoteAddr()}
            );
            return Response.ok().build();
        } else {
            LOGGER.log(Level.INFO,
                    "Unsuccessful group change to {0} for user {1} from IP {2}",
                    new Object[]{group, securityContext.getUserPrincipal().getName(),
                            httpServletRequest.getRemoteAddr()}
            );
            throw ExceptionFactory.userNotInGroupException();
        }
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
    public Response refreshToken(ContainerRequestContext context) throws BaseApplicationException {
        JWT oldToken = getJwtFromAuthHeader(context.getHeaderString("Authorization"));

        List<String> newGroups = accountEndpoint.getAccountGroups(oldToken.subject);

        return Response.ok().entity(new AuthTokenDto(refresh(oldToken, newGroups))).build();
    }
}
