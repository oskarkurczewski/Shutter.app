package pl.lodz.p.it.ssbd2022.ssbd02.exceptions;

import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.ErrorDto;

import javax.ws.rs.core.Response;


/**
 * Klasa wyjątku reprezentująca podanie nieprawidłowego parametru w zapytaniu
 */
public class WrongParameterException extends BaseApplicationException{
    public WrongParameterException(String message) {
        super(Response.status(Response.Status.BAD_REQUEST).entity(new ErrorDto(message)).build());
    }
}
