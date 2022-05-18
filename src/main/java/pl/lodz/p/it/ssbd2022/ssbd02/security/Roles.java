package pl.lodz.p.it.ssbd2022.ssbd02.security;

/**
 * Klasa przechowująca obecne w systemie role w postaci ciągów znakowych
 */
public class Roles {
    public static final String ADMINISTRATOR = "ADMINISTRATOR";
    public static final String MODERATOR = "MODERATOR";
    public static final String CLIENT = "CLIENT";
    public static final String PHOTOGRAPHER = "PHOTOGRAPHER";
    public static final String refreshToken = "refreshToken";
    public static final String registerAccount = "registerAccount";
    public static final String createAccount = "createAccount";
    public static final String blockAccount = "blockAccount";
    public static final String unblockAccount = "unblockAccount";
    public static final String grantAccessLevel = "grantAccessLevel";
    public static final String revokeAccessLevel = "revokeAccessLevel";
    public static final String changeOwnPassword = "changeOwnPassword";
    public static final String changeSomeonesPassword = "changeSomeonesPassword";
    public static final String editOwnAccountData = "editOwnAccountData";
    public static final String editSomeonesAccountData = "editSomeonesAccountData";
    public static final String getAccountInfo = "getAccountInfo";
    public static final String listAllAccounts = "listAllAccounts";
    public static final String becomePhotographer = "becomePhotographer";
    public static final String stopBeingPhotographer = "stopBeingPhotographer";
    public static final String changePhotographerDescription = "changePhotographerDescription";
    public static final String reviewPhotographer = "reviewPhotographer";
    public static final String deleteOwnPhotographerReview = "deleteOwnPhotographerReview";
    public static final String deleteSomeonesPhotographerReview = "deleteSomeonesPhotographerReview";
    public static final String reportPhotographer = "reportPhotographer";
    public static final String reportClient = "reportClient";
    public static final String reportReview = "reportReview";
    public static final String likeReview = "likeReview";
    public static final String unlikeReview = "unlikeReview";
    public static final String addPhotoToGallery = "addPhotoToGallery";
    public static final String deletePhotoFromGallery = "deletePhotoFromGallery";
    public static final String likePhoto = "likePhoto";
    public static final String unlikePhoto = "unlikePhoto";
    public static final String listAllReports = "listAllReports";
    public static final String changeAvailabilityHours = "changeAvailabilityHours";
    public static final String reservePhotographer = "reservePhotographer";
    public static final String cancelReservation = "cancelReservation";
    public static final String discardReservation = "discardReservation";
    public static final String showReservations = "showReservations";
    public static final String showJobs = "showJobs";
}
