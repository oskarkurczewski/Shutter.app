package pl.lodz.p.it.ssbd2022.ssbd02.exceptions;


public class ExceptionFactory {


    public static BadJWTTokenException badJWTTokenException() {
        return new BadJWTTokenException("exception.token");
    }

    public static DatabaseException databaseException() {
        return new DatabaseException("exception.database");
    }

    public static DataNotFoundException dataNotFoundException() {
        return new DataNotFoundException("exception.notfound");
    }

    public static EmailException emailException(String message) {
        return new EmailException("exception.email");
    }

    public static IdenticalFieldException identicalFieldException(String message) {
        return new IdenticalFieldException(message);
    }

    public static NoAccountFound noAccountFound() {
        return new NoAccountFound("exception.account.notfound");
    }

    public static NoAuthenticatedAccountFound noAuthenticatedAccountFound() {
        return new NoAuthenticatedAccountFound("exception.account.unauthenticated");
    }

    public static NoConfigFileFound noConfigFileFound() {
        return new NoConfigFileFound();
    }

    public static NoPhotographerFound noPhotographerFound() {
        return new NoPhotographerFound("exception.photographer.notfound");
    }

    public static PasswordMismatchException passwordMismatchException() {
        return new PasswordMismatchException();
    }

    public static WrongPasswordException wrongPasswordException() {
        return new WrongPasswordException("mes");
    }

    public static UnexpectedFailException unexpectedFailException() {
        return new UnexpectedFailException("exception.unexpected");
    }


}
