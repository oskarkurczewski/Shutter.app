package pl.lodz.p.it.ssbd2022.ssbd02.exceptions;

import javax.ws.rs.core.Response;

/**
 * Klasa wyjątku reprezentująca podanie żetonu, który wygasł
 */
public class ExpiredTokenException extends BaseApplicationException {
    public ExpiredTokenException(String msg) {
        super(Response.status(Response.Status.BAD_REQUEST).entity(msg).build());
    }
}
