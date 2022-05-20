package pl.lodz.p.it.ssbd2022.ssbd02.exceptions;

import javax.ws.rs.core.Response;

/**
 * Klasa wyjątku reprezentująca nieoczekiwany błąd występujący gdy nie został złapany żaden inny wyjątek
 */
public class UnexpectedFailException extends BaseApplicationException {
    public UnexpectedFailException(String message) {
        super(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(message).build());
    }
}
