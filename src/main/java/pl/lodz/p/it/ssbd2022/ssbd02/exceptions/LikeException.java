package pl.lodz.p.it.ssbd2022.ssbd02.exceptions;

import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.ErrorDto;

import javax.ws.rs.core.Response;

public class LikeException extends BaseApplicationException{
    public LikeException(String message) {
        super(Response.status(Response.Status.CONFLICT).entity(new ErrorDto(message)).build());
    }
}
