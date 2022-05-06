package pl.lodz.p.it.ssbd2022.ssbd02.exceptions;

import javax.ws.rs.core.Response;

public class UnauthenticatedException extends BaseApplicationException{
    public UnauthenticatedException(String message) {
        super(Response.status(Response.Status.UNAUTHORIZED).entity(message).build());
    }
}
