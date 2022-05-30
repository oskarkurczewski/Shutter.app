package pl.lodz.p.it.ssbd2022.ssbd02.mok.dto;

import lombok.Data;
import pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint.*;

import javax.validation.constraints.NotNull;


/**
 * Klasa reprezentująca obiekt transferu danych reprezentujący wymagane dane użytkownika
 * służące do utworzenia konta użytkownika.
 * Na pola klasy nałożone zostały ograniczenia.
 */
@Data
public class AccountRegisterDto {

    @NotNull(message = "validator.incorrect.login.null")
    @Login
    private String login;

    @NotNull(message = "validator.incorrect.password.null")
    @Password
    private String password;

    @NotNull(message = "validator.incorrect.email.null")
    @Email
    private String email;

    @NotNull(message = "validator.incorrect.name.null")
    @Name
    private String name;

    @NotNull(message = "validator.incorrect.surname.null")
    @Surname
    private String surname;

    @NotNull(message = "validator.incorrect.re_captcha.null")
    private String reCaptchaToken;
}
