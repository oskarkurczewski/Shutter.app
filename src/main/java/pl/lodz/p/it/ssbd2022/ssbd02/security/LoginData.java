package pl.lodz.p.it.ssbd2022.ssbd02.security;

import lombok.Data;
import pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint.Login;
import pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint.Password;
import pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint.TwoFACode;

import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.validation.constraints.NotNull;

/**
 * Klasa reprezentująca przesłane przez użytkownika poświadczenia w postaci loginu oraz hasła.
 * Udostępnia metodę fabrykującą obiekt klasy UsernamePasswordCredential służący później do uwierzytelnienia.
 */
@Data
public class LoginData {

    @NotNull(message = "validator.incorrect.login.null")
    @Login
    private String login;

    @NotNull(message = "validator.incorrect.password.null")
    private String password;

    @TwoFACode
    private String twoFACode;

    /**
     * Metoda fabrykująca obiekt klasy UsernamePasswordCredential
     *
     * @return Obiekt klasy UsernamePasswordCredential
     */
    public Credential getCredential() {
        return new UsernamePasswordCredential(login, password);
    }

}
