package pl.lodz.p.it.ssbd2022.ssbd02.exceptions;

import javax.ws.rs.core.Response;

/**
 * Klasa wyjątku reprezentująca nieznalezienie pliku konfiguracyjnego
 */
public class NoConfigFileFound extends BaseApplicationException {
    public NoConfigFileFound() {
        super(Response.status(Response.Status.INTERNAL_SERVER_ERROR).build());
    }
}
