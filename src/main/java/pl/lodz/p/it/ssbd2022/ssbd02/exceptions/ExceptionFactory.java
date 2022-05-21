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
        return new PasswordMismatchException("exception.password.mismatch");
    }

    public static WrongPasswordException wrongPasswordException() {
        return new WrongPasswordException("mes");
    }

    public static UnexpectedFailException unexpectedFailException() {
        return new UnexpectedFailException("exception.unexpected");
    }

    public static AvailabilityOverlapException availabilityOverlapException() {
        return new AvailabilityOverlapException("exception.availability.overlap");
    }

    public static NoAvailabilityFoundException noAvailabilityFoundException() {
        return new NoAvailabilityFoundException("exception.availability.not_found");
    }

    public static InvalidReservationTimeExcpetion invalidReservationTimeException() {
        return new InvalidReservationTimeExcpetion("exception.reservation.invalid_time");
    }

    public static NoReservationFoundException noReservationFoundException() {
        return new NoReservationFoundException("exception.reservation.not_found");
    }

    public static NoPhotographerFoundException noPhotographerFoundException() {
        return new NoPhotographerFoundException("exception.photographer.not_found");
    }

    public static NoReviewFoundException noReviewFoundException() {
        return new NoReviewFoundException("exception.review.not_found");
    }

    public static NoPhotoFoundException noPhotoFoundException() {
        return new NoPhotoFoundException("exception.photo.not_found");
    }

    public static NoAccountReportFoundException noAccountReportFoundException() {
        return new NoAccountReportFoundException("exception.account_report.not_found");
    }

    public static NoPhotographerReportFoundException noPhotographerReportFoundException() {
        return new NoPhotographerReportFoundException("exception.photographer_report.not_found");
    }

    public static NoReviewReportFoundException noReviewReportFoundException() {
        return new NoReviewReportFoundException("exception.review_report.not_found");
    }

    public static AccountConfirmedException accountConfirmedException() {
        return new AccountConfirmedException("exception.account.already_confirmed");
    }

    public static NoVerificationTokenFound noVerificationTokenFound() {
        return new NoVerificationTokenFound("exception.token.not_found");
    }

    public static ExpiredTokenException expiredTokenException() {
        return new ExpiredTokenException("exception.token.expired");
    }

    public static InvalidTokenException invalidTokenException() {
        return new InvalidTokenException("exception.token.invalid");
    }

    public static BaseApplicationException OptLockException() {
        return new OptLockException();
    }
}
