package pl.lodz.p.it.ssbd2022.ssbd02.exceptions;

import javax.ws.rs.core.Response;

/**
 * Klasa wyjąku reprezentująca próbę potwierdzenia konta które zostało już wcześniej potwierdzone.
 */
public class AccountConfirmedException extends BaseApplicationException {
    public AccountConfirmedException(String message) {
        super(Response.status(Response.Status.BAD_REQUEST).entity(message).build());
    }
}