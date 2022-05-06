package pl.lodz.p.it.ssbd2022.ssbd02.exceptions;

public class NoAuthenticatedUserFound extends CustomApplicationException {

    public NoAuthenticatedUserFound(String message) {
        super(message);
    }
}
