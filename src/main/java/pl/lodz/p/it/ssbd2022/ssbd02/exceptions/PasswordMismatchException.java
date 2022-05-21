package pl.lodz.p.it.ssbd2022.ssbd02.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Klasa wyjątku reprezentująca wyjątek nie zgodności haseł, która jest wymagana w celu zmiany hasła
 */
public class PasswordMismatchException extends BaseApplicationException {
    public PasswordMismatchException(String msg) {
        super(Response.status(Response.Status.BAD_REQUEST).entity(msg).build());
    }
}
