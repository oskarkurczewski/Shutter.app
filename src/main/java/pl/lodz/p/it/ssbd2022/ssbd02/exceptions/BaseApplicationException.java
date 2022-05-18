package pl.lodz.p.it.ssbd2022.ssbd02.exceptions;

import lombok.Getter;

import javax.ws.rs.core.Response;

/**
 * Bazowa klasa wyjątków aplikacji, nie powinna być łapana przez obsługę wyjątków,
 * powinny być łapane klasy rozszerzające tę metodę.
 */
public abstract class BaseApplicationException extends Exception {

    @Getter
    private final Response response;

    public BaseApplicationException(Response response) {
        super();
        this.response = response;
    }
}
