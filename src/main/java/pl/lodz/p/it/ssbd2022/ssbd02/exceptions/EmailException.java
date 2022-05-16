package pl.lodz.p.it.ssbd2022.ssbd02.exceptions;

import javax.ws.rs.core.Response;

/**
 * Klasa wyjątku reprezentująca wyjątki w bibliotece wysyłającej email
 */
public class EmailException extends BaseApplicationException {
    public EmailException(String message) {
        super(Response.status(Response.Status.NOT_FOUND).entity(message).build());
    }
}
