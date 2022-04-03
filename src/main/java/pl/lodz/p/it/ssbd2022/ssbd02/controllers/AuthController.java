package pl.lodz.p.it.ssbd2022.ssbd02.controllers;


import pl.lodz.p.it.ssbd2022.ssbd02.security.JWTHandler;
import pl.lodz.p.it.ssbd2022.ssbd02.util.LoginData;
import pl.lodz.p.it.ssbd2022.ssbd02.util.PasswordHashImpl;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStoreHandler;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Path("auth")
public class AuthController {

    @Inject
    private IdentityStoreHandler storeHandler;

    @Inject
    SecurityContext securityContext;

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(@NotNull LoginData data) {
        CredentialValidationResult validationResult = storeHandler.validate(data.getCredential());

        System.out.println(validationResult.getStatus().toString());

        if (validationResult.getStatus() == CredentialValidationResult.Status.VALID) {
            String token = JWTHandler.generateJWT(validationResult);

            return Response.ok().entity(token).build();
        }
        // TODO Wyciągnąć do wyjątku / internacjonalizacja
        return Response.ok().entity("Wrong login or password!").build();
    }

}
