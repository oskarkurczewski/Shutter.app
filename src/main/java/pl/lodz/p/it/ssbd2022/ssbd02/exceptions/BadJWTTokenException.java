package pl.lodz.p.it.ssbd2022.ssbd02.exceptions;

import javax.ws.rs.core.Response;

public class BadJWTTokenException extends BaseApplicationException {

    public BadJWTTokenException(String message) {
        super(Response.status(Response.Status.UNAUTHORIZED).entity(message).build());
    }
}
