package pl.lodz.p.it.ssbd2022.ssbd02.exceptions;

public abstract class CustomApplicationException extends Exception {
    public CustomApplicationException() {
    }

    public CustomApplicationException(String message) {
        super(message);
    }

    public static NoAuthenticatedUser NoAuthenticatedUser() {
        return new NoAuthenticatedUser("No authenticated user");
    }
}
