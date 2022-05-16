package pl.lodz.p.it.ssbd2022.ssbd02.mappers;

import javax.ejb.AccessLocalException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Klasa mapująca wyjątek występujący, gdy użytkownik nie ma dostępu do zasobu, pozwalająca na modyfikację
 * zachowania w przypadku jego wystąpienia.
 */
@Provider
public class AccessLocalExceptionMapper implements ExceptionMapper<AccessLocalException> {
    @Override
    public Response toResponse(AccessLocalException e) {
        return Response.status(Response.Status.UNAUTHORIZED).entity("exception.account.unauthorized").build();
    }
}
