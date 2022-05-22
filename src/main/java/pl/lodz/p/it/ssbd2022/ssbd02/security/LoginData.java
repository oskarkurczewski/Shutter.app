package pl.lodz.p.it.ssbd2022.ssbd02.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;

/**
 * Klasa reprezentująca przesłane przez użytkownika poświadczenia w postaci loginu oraz hasła.
 * Udostępnia metodę fabrykującą obiekt klasy UsernamePasswordCredential służący później do uwierzytelnienia.
 */
@Getter
@Setter
@NoArgsConstructor
public class LoginData {

    private String login;
    private String password;
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
