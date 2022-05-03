package pl.lodz.p.it.ssbd2022.ssbd02.exceptions;

import lombok.Getter;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class BaseApplicationException extends Exception {

    @Getter
    private Response response;

    public BaseApplicationException(Response response) {
        super();
        this.response = response;
    }
}
