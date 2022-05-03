package pl.lodz.p.it.ssbd2022.ssbd02.exceptions;

import javax.ws.rs.core.Response;

public class IdenticalFieldException extends BaseApplicationException {
    public IdenticalFieldException(String message) {
        super(Response.status(Response.Status.BAD_REQUEST).entity(message).build());
    }
}
