package pl.lodz.p.it.ssbd2022.ssbd02.mappers;

import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.ErrorDto;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Objects;

/**
 * Klasa mapująca wyjątek naruszenia ograniczeń, pozwalająca na modyfikację
 * zachowania w przypadku jego wystąpienia.
 *
 * @see ConstraintViolationException
 */
@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {


    @Override
    public Response toResponse(ConstraintViolationException e) {
        return Response.status(Response.Status.BAD_REQUEST).entity(new ErrorDto(getMessage(e))).build();
    }

    private String getMessage(ConstraintViolationException exception) {
        StringBuilder builder = new StringBuilder();
        for (ConstraintViolation<?> constraintViolation :
                exception.getConstraintViolations()) {
            builder.append(constraintViolation.getMessage())
                    .append(':')
                    .append(Objects.toString(constraintViolation.getInvalidValue(), ""))
                    .append(',');
        }
        return builder.toString();
    }
}
