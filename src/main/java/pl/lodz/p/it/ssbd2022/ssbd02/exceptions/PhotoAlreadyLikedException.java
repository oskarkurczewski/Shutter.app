package pl.lodz.p.it.ssbd2022.ssbd02.exceptions;

import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.ErrorDto;

import javax.ws.rs.core.Response;

/**
 * Klasa wyjątku reprezentująca wyjątek wystepujący w przypadku próby polubienia już polubionego zdjęcia
 */
public class PhotoAlreadyLikedException extends BaseApplicationException {
    public PhotoAlreadyLikedException(String message) {
        super(Response.status(Response.Status.BAD_REQUEST).entity(new ErrorDto(message)).build());
    }
}
