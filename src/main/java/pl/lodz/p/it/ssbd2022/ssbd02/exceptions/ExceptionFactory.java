package pl.lodz.p.it.ssbd2022.ssbd02.exceptions;


public class ExceptionFactory {


    public static BadJWTTokenException badJWTTokenException() {
        return new BadJWTTokenException("exception.bad_auth_token");
    }

    public static DatabaseException databaseException() {
        return new DatabaseException("exception.database");
    }

    public static CannotChangeException cannotChangeException() {
        return new CannotChangeException("exception.cannot_change");
    }

    public static DataNotFoundException dataNotFoundException() {
        return new DataNotFoundException("exception.notfound");
    }

    public static EmailException emailException(String message) {
        return new EmailException("exception.mail_provider_error");
    }

    public static IdenticalFieldException identicalFieldException(String message) {
        return new IdenticalFieldException(message);
    }

    public static NoAccountFound noAccountFound() {
        return new NoAccountFound("exception.account_not_found");
    }

    public static NoAuthenticatedAccountFound noAuthenticatedAccountFound() {
        return new NoAuthenticatedAccountFound("exception.account_unauthenticated");
    }

    public static NoConfigFileFound noConfigFileFound() {
        return new NoConfigFileFound();
    }

    public static NoPhotographerFound noPhotographerFound() {
        return new NoPhotographerFound("exception.photographer_not_found");
    }

    public static PasswordMismatchException passwordMismatchException() {
        return new PasswordMismatchException("exception.password_mismatch");
    }

    public static WrongPasswordException wrongPasswordException() {
        return new WrongPasswordException("exception.old_password_not_exists");
    }

    public static UnexpectedFailException unexpectedFailException() {
        return new UnexpectedFailException("exception.unexpected");
    }

    public static TwoFARequiredException twoFARequiredException() {
        return new TwoFARequiredException("exception.2_fa_code_required");
    }

    public static AvailabilityOverlapException availabilityOverlapException() {
        return new AvailabilityOverlapException("exception.availability_overlap");
    }

    public static NoAvailabilityFoundException noAvailabilityFoundException() {
        return new NoAvailabilityFoundException("exception.availability_not_found");
    }

    public static InvalidReservationTimeExcpetion invalidReservationTimeException() {
        return new InvalidReservationTimeExcpetion("exception.reservation_invalid_time");
    }

    public static NoReservationFoundException noReservationFoundException() {
        return new NoReservationFoundException("exception.reservation_not_found");
    }

    // istnieje już podobny wyjątek noPhotographerFound do podmianki / zostawienia?
    public static NoPhotographerFoundException noPhotographerFoundException() {
        return new NoPhotographerFoundException("exception.photographer_not_found");
    }

    public static NoReviewFoundException noReviewFoundException() {
        return new NoReviewFoundException("exception.review_not_found");
    }

    public static NoPhotoFoundException noPhotoFoundException() {
        return new NoPhotoFoundException("exception.photo_not_found");
    }

    public static NoAccountReportFoundException noAccountReportFoundException() {
        return new NoAccountReportFoundException("exception.account_report_not_found");
    }

    public static NoPhotographerReportFoundException noPhotographerReportFoundException() {
        return new NoPhotographerReportFoundException("exception.photographer_report_not_found");
    }

    public static NoReviewReportFoundException noReviewReportFoundException() {
        return new NoReviewReportFoundException("exception.review_report_not_found");
    }

    public static AccountConfirmedException accountConfirmedException() {
        return new AccountConfirmedException("exception.account_already_confirmed");
    }

    public static NoVerificationTokenFound noVerificationTokenFound() {
        return new NoVerificationTokenFound("exception.token_not_found");
    }

    public static ExpiredTokenException expiredTokenException() {
        return new ExpiredTokenException("exception.token_expired");
    }

    public static InvalidTokenException invalidTokenException() {
        return new InvalidTokenException("exception.token_invalid");
    }

    public static BaseApplicationException OptLockException() {
        return new OptLockException("exception.optlock");
    }

    public static WrongParameterException wrongParameterException() {
        return new WrongParameterException("exception.search_wrong_parameter");
    }

    public static UserNotInGroupException userNotInGroupException() {
        return new UserNotInGroupException("exception.user_not_in_group");
    }

    public static NonUniquePasswordException nonUniquePasswordException() {
        return new NonUniquePasswordException("exception.password_not_unique");
    }

    public static InvalidRecaptchaException invalidRecaptchaException() {
        return new InvalidRecaptchaException("exception.captcha_invalid");
    }

    public static ETagException etagException() {
        return new ETagException("exception.etag");
    }

    public static NoAccountListPreferencesFound noAccountListPreferencesFound() {
        return new NoAccountListPreferencesFound("exception.account_list_preferences_not_found");
    }

    public static PhotoAlreadyLikedException photoAlreadyLikedException() {
        return new PhotoAlreadyLikedException("photo.liked");
    }
}
