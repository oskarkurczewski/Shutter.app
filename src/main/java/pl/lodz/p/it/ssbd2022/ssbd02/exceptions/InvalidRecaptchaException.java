package pl.lodz.p.it.ssbd2022.ssbd02.exceptions;

import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.ErrorDto;

import javax.ws.rs.core.Response;

public class InvalidRecaptchaException extends BaseApplicationException {
    public InvalidRecaptchaException(String msg) {
        super(Response.status(Response.Status.BAD_REQUEST).entity(new ErrorDto(msg)).build());
    }
}
