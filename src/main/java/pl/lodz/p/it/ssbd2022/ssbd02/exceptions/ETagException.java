package pl.lodz.p.it.ssbd2022.ssbd02.exceptions;

import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.ErrorDto;

import javax.ws.rs.core.Response;

public class ETagException extends BaseApplicationException {
    public ETagException(String message) {
        super(Response.status(Response.Status.PRECONDITION_FAILED).entity(new ErrorDto(message)).build());
    }
}
