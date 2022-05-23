package pl.lodz.p.it.ssbd2022.ssbd02.exceptions;


import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.ErrorDto;

import javax.ws.rs.core.Response;

/**
 * Klasa wyjątku reprezentująca niemożliwość aktualizacji wartości.
 * Mapowany na kod odpowiedzi 409 - Conflict
 */
public class CannotChangeException extends BaseApplicationException{
    public CannotChangeException(String message) {
        super(Response.status(Response.Status.CONFLICT).entity(new ErrorDto(message)).build());
    }
}
