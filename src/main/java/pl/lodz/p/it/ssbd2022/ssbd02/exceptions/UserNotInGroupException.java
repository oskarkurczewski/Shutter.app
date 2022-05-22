package pl.lodz.p.it.ssbd2022.ssbd02.exceptions;

import javax.ws.rs.core.Response;

public class UserNotInGroupException extends BaseApplicationException {
    public UserNotInGroupException(String msg) {
        super(Response.status(Response.Status.UNAUTHORIZED).entity(msg).build());
    }
}
