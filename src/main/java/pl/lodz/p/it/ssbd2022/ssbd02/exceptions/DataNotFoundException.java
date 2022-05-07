package pl.lodz.p.it.ssbd2022.ssbd02.exceptions;

import javax.ws.rs.core.Response;

/**
 * Klasa wyjątku reprezentująca nieznalezienie poszukiwanej wartości.
 * Mapowany na kod odpowiedzi 404 - NotFound
 */
public class DataNotFoundException extends BaseApplicationException{
    public DataNotFoundException(String message) {
        super(Response.status(Response.Status.NOT_FOUND).entity(message).build());
    }
}