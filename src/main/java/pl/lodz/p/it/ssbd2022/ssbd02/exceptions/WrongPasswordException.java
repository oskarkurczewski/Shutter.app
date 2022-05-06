package pl.lodz.p.it.ssbd2022.ssbd02.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Klasa reprezentująca wyjątek rzucany w momencie próby zmiany hasła
 * na niespełniające wymagań, mapowany na kod odpowiedzi 400 przez kontroler
 */
public class WrongPasswordException extends WebApplicationException {
    public WrongPasswordException(String message) {
        super(Response.status(Response.Status.BAD_REQUEST).entity(message).build());
    }
}
