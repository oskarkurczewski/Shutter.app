package pl.lodz.p.it.ssbd2022.ssbd02.mappers;

import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Klasa mapująca wyjątki bazowe aplikacji na odpowiedź HTTP poprzez implementację
 * interfejsu ExceptionMapper
 */
@Provider
public class BaseApplicationExceptionMapper implements ExceptionMapper<BaseApplicationException> {
    @Override
    public Response toResponse(BaseApplicationException e) {
        return e.getResponse();
    }
}