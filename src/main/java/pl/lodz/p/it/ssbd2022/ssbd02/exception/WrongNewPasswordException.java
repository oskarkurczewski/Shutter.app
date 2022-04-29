package pl.lodz.p.it.ssbd2022.ssbd02.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Klasa reprezentująca wyjątek rzucany w momencie próby zmiany hasła
 * na niespełniające wymagań, mapowany na kod odpowiedzi 400 przez kontroler
 */
public class WrongNewPasswordException extends WebApplicationException {
    public WrongNewPasswordException(String message) {
        super(Response.status(Response.Status.BAD_REQUEST).entity(message).build());
    }
}
