package pl.lodz.p.it.ssbd2022.ssbd02.exceptions;

import com.squareup.okhttp.Request;

import javax.ws.rs.core.Response;

/**
 * Wyjątek informujący o tym, że próba utworzenia danego przedziału godzin dostępności nie powiodła
 * się z racji na to, iż nachodzi on na już istniejące godziny.
 *
 * Jest mapowane na kod błedu 400 BAD_REQUEST
 */
public class AvailabilityOverlapException extends BaseApplicationException {

    public AvailabilityOverlapException(String message) {
        super(Response.status(Response.Status.BAD_REQUEST).entity(message).build());
    }
}
