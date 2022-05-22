package pl.lodz.p.it.ssbd2022.ssbd02.exceptions;

import javax.ws.rs.core.Response;

public class OptLockException extends BaseApplicationException {
    public OptLockException() {
        super(Response.status(Response.Status.BAD_REQUEST).build());
    }
}
