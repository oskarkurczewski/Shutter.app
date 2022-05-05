package pl.lodz.p.it.ssbd2022.ssbd02.exceptions;

public class NoAuthenticatedUser extends CustomApplicationException {

    public NoAuthenticatedUser(String message) {
        super(message);
    }
}
