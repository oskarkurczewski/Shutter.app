package pl.lodz.p.it.ssbd2022.ssbd02.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class BaseApplicationExceptionMapper implements ExceptionMapper<BaseApplicationException> {
    @Override
    public Response toResponse(BaseApplicationException e) {
        return e.getResponse();
    }
}