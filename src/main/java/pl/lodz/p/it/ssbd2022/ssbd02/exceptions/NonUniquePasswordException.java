package pl.lodz.p.it.ssbd2022.ssbd02.exceptions;

import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.ErrorDto;

import javax.ws.rs.core.Response;

/**
 * Reprezentuje próbę zmiany hasła na jedno z historycznych dla danego użytkownika
 */
public class NonUniquePasswordException extends BaseApplicationException {
    public NonUniquePasswordException(String message) {
        super(Response.status(Response.Status.BAD_REQUEST).entity(new ErrorDto(message)).build());
    }
}
