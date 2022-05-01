package pl.lodz.p.it.ssbd2022.ssbd02.validation.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginValidator {

    public static final String LOGIN_PATTERN
            = "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){1,13}[a-zA-Z0-9]$";

    public static final Pattern pattern = Pattern.compile(LOGIN_PATTERN);

    public static boolean isValid(final String login) {
        Matcher matcher = pattern.matcher(login);
        return matcher.matches();
    }

}
