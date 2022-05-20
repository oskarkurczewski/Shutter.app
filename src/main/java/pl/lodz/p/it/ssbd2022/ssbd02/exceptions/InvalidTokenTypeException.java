package pl.lodz.p.it.ssbd2022.ssbd02.exceptions;

import javax.ws.rs.core.Response;

/**
 * Klasa wyjąku reprezentująca podanie złego typu żetonu weryfikacyjnego.
 */
public class InvalidTokenTypeException extends BaseApplicationException {
    public InvalidTokenTypeException(String message) {
        super(Response.status(Response.Status.BAD_REQUEST).entity(message).build());
    }
}
