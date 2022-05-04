package pl.lodz.p.it.ssbd2022.ssbd02.exceptions;

public abstract class CustomApplicationException extends Exception {
    public CustomApplicationException() {
    }

    public CustomApplicationException(String message) {
        super(message);
    }

    public static NoAuthenticatedUserFound NoAuthenticatedUserFound() {
        return new NoAuthenticatedUserFound("No authenticated user found");
    }

    public static NoUserFound NoUserFound() {
        return new NoUserFound("No user found");
    }
}
