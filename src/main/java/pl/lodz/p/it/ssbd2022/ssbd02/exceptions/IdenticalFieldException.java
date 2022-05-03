package pl.lodz.p.it.ssbd2022.ssbd02.exceptions;

import javax.ws.rs.core.Response;

public class IdenticalFieldException extends BaseApplicationException {

    public IdenticalFieldException(String message) {
        super(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(message).build());
    }
}
