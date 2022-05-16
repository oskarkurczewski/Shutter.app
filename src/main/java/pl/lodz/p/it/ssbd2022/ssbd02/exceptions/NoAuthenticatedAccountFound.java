package pl.lodz.p.it.ssbd2022.ssbd02.exceptions;

import javax.ws.rs.core.Response;

/**
 * Klasa wyjątku reprezentująca nieznalezienie konta użytkownika w bazie danych na podstawie tożsamości z tokenu JWT
 */
public class NoAuthenticatedAccountFound extends BaseApplicationException {
    public NoAuthenticatedAccountFound(String message) {
        super(Response.status(Response.Status.UNAUTHORIZED).entity(message).build());
    }
}
