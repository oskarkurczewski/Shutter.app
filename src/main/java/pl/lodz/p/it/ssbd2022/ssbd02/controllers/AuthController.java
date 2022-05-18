package pl.lodz.p.it.ssbd2022.ssbd02.controllers;


import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BadJWTTokenException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.ExceptionFactory;
import pl.lodz.p.it.ssbd2022.ssbd02.security.JWTHandler;
import pl.lodz.p.it.ssbd2022.ssbd02.security.LoginData;

import javax.inject.Inject;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStoreHandler;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


/**
 * Kontroler udostępniający końcówki obsługujące uwierzytelnianie
 * Ścieżka zasobu to /api/auth
 */
@Path("auth")
public class AuthController {

    @Inject
    private IdentityStoreHandler storeHandler;

    /**
     * Pozwala na uwierzytelnienie użytkownika weryfikując podane przez niego poświadczenia.
     * W przypadku powodzenia zwracany jest żeton JWT, w przeciwnym wypadku zwracany jest stosowny komunikat.
     * Ścieżka zasobu to /api/auth/login.
     *
     * @param data Obiekt klasy LoginData reprezentujący przesłany przez użytkownika login oraz hasło
     * @return Obiekt klasy Response zawierający JWT lub komunikat tekstowy
     * @see LoginData
     */
    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(@NotNull LoginData data) throws BadJWTTokenException {
        CredentialValidationResult validationResult = storeHandler.validate(data.getCredential());

        if (validationResult.getStatus() == CredentialValidationResult.Status.VALID) {
            String token = JWTHandler.generateJWT(validationResult);

            return Response.ok().entity(token).build();
        }
        throw ExceptionFactory.badJWTTokenException();
    }

}
