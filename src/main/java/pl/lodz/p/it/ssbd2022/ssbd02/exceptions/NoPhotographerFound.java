package pl.lodz.p.it.ssbd2022.ssbd02.exceptions;

import javax.ws.rs.core.Response;

/**
 * Klasa wyjątku reprezentująca nieznalezienie danego konta fotografa
 */
public class NoPhotographerFound extends BaseApplicationException {
    public NoPhotographerFound(String message) {
        super(Response.status(Response.Status.NOT_FOUND).entity(message).build());
    }
}
