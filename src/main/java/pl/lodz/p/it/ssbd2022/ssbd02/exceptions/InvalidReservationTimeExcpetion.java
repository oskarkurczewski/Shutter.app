package pl.lodz.p.it.ssbd2022.ssbd02.exceptions;

import javax.ws.rs.core.Response;

public class InvalidReservationTimeExcpetion extends BaseApplicationException {

    public InvalidReservationTimeExcpetion(String msg) {
        super(Response.status(Response.Status.BAD_REQUEST).entity(msg).build());
    }
}
