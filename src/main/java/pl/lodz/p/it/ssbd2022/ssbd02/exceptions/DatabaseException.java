package pl.lodz.p.it.ssbd2022.ssbd02.exceptions;

import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.ErrorDto;

import javax.ws.rs.core.Response;

/**
 * Klasa wyjątku reprezentująca błąd po stronie bazy danych
 */
public class DatabaseException extends BaseApplicationException {
    public DatabaseException(String message) {
        super(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new ErrorDto(message)).build());
    }
}
