package pl.lodz.p.it.ssbd2022.ssbd02.util;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import java.util.Locale;
import java.util.ResourceBundle;

@Stateless
public class I18n {
    public static String REGISTRATION_CONFIRMATION_SUBJECT = "registration.confirmation.subject";
    public static String REGISTRATION_CONFIRMATION_BODY = "registration.confirmation.body";
    public static String REGISTRATION_CONFIRMATION_REMINDER_SUBJECT = "registration.confirmation.reminder.subject";
    public static String REGISTRATION_CONFIRMATION_REMINDER_BODY = "registration.confirmation.reminder.body";
    public static String PASSWORD_RESET_SUBJECT = "password.reset.subject";
    public static String PASSWORD_RESET_BODY = "password.reset.body";
    public static String FORCED_PASSWORD_RESET_SUBJECT = "forced.password.reset.subject";
    public static String FORCED_PASSWORD_RESET_REMINDER_BODY = "forced.password.reset.reminder.body";
    public static String INACTIVE_ACCOUNT_BLOCK_SUBJECT = "inactive.account.block.subject";
    public static String INACTIVE_ACCOUNT_BLOCK_BODY = "inactive.account.block.body";
    public static String ADMIN_AUTHENTICATION_WARNING_SUBJECT = "admin.authentication.warning.subject";
    public static String ADMIN_AUTHENTICATION_WARNING_BODY = "admin.authentication.warning.body";
    public static String EMAIL_UPDATE_SUBJECT = "email.update.subject";
    public static String EMAIL_UPDATE_BODY = "email.update.body";
    public static String EMAIL_UPDATE_REMINDER_SUBJECT = "email.update.reminder.subject";
    public static String EMAIL_UPDATE_REMINDER_BODY = "email.update.reminder.body";
    public static String TWO_FA_SUBJECT = "two_fa.subject";
    public static String TWO_FA_BODY = "two_fa.body";
    public static String ACCOUNT_UNBLOCKED_SUBJECT = "account.unblocked.subject";
    public static String ACCOUNT_UNBLOCKED_BODY = "account.unblocked.body";
    public static String UNBLOCK_ACCOUNT_SUBJECT = "unblock.account.subject";
    public static String UNBLOCK_ACCOUNT_BODY = "unblock.account.body";
    public static String ACCOUNT_BLOCKED_SUBJECT = "account.blocked.subject";
    public static String ACCOUNT_BLOCKED_BODY = "account.blocked.body";
    public static String ACCESS_LEVEL_GRANTED_SUBJECT = "access.level.granted.subject";
    public static String ACCESS_LEVEL_GRANTED_BODY = "access.level.granted.body";
    public static String ACCESS_LEVEL_REVOKED_SUBJECT = "access.level.revoked.subject";
    public static String ACCESS_LEVEL_REVOKED_BODY = "access.level.revoked.body";
    public static String ACCOUNT_ACTIVATED_SUBJECT = "account.activated.subject";
    public static String ACCOUNT_ACTIVATED_BODY = "account.activated.body";
    public static String RESERVATION_CANCELED = "reservation.canceled.subject";
    public static String RESERVATION_CANCELED_BODY = "reservation.canceled.body";
    public static String RESERVATION_DISCARDED = "reservation.discarded.subject";
    public static String RESERVATION_DISCARDED_BODY = "reservation.discarded.body";

    @PermitAll
    public String getMessage(String key, Locale locale) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("i18n", locale);
        return resourceBundle.getString(key);
    }
}
