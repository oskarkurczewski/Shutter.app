package pl.lodz.p.it.ssbd2022.ssbd02.exceptions;

import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.ErrorDto;

import javax.ws.rs.core.Response;

public class NoReservationFoundException extends BaseApplicationException {

    public NoReservationFoundException(String message) {
        super(Response.status(Response.Status.NOT_FOUND).entity(new ErrorDto(message)).build());
    }
}
