package pl.lodz.p.it.ssbd2022.ssbd02.validation.mapper;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {


    @Override
    public Response toResponse(ConstraintViolationException e) {
        return Response.status(Response.Status.BAD_REQUEST).entity(getMessage(e)).build();
    }

    private String getMessage(ConstraintViolationException exception) {
        StringBuilder builder = new StringBuilder();
        for (ConstraintViolation<?> constraintViolation :
                exception.getConstraintViolations()) {
            builder.append(constraintViolation.getInvalidValue())
                    .append(':')
                    .append(constraintViolation.getMessage())
                    .append(",");
        }
        return builder.toString();
    }
}
