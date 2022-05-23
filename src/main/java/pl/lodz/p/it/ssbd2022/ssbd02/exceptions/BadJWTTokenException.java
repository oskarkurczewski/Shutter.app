package pl.lodz.p.it.ssbd2022.ssbd02.exceptions;

import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.ErrorDto;

import javax.ws.rs.core.Response;

public class BadJWTTokenException extends BaseApplicationException {

    public BadJWTTokenException(String message) {
        super(Response.status(Response.Status.UNAUTHORIZED).entity(new ErrorDto(message)).build());
    }
}
