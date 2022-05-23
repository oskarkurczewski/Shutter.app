package pl.lodz.p.it.ssbd2022.ssbd02.exceptions;

import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.ErrorDto;

import javax.ws.rs.core.Response;

/**
 * Klasa wyjątku reprezentująca brak znalezienia żetonu weryfikacyjnego w bazie
 */
public class NoVerificationTokenFound extends BaseApplicationException {
    public NoVerificationTokenFound(String message) {
        super(Response.status(Response.Status.NOT_FOUND).entity(new ErrorDto(message)).build());
    }
}
