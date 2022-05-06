package pl.lodz.p.it.ssbd2022.ssbd02.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class PasswordMismatchException extends WebApplicationException {
    public PasswordMismatchException() {
        super(Response.status(Response.Status.BAD_REQUEST).build());
    }
}
