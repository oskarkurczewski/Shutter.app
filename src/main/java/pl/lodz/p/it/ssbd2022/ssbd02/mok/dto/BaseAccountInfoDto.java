package pl.lodz.p.it.ssbd2022.ssbd02.mok.dto;

import lombok.Data;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.security.etag.SignableEntity;
import pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint.Login;

import javax.validation.constraints.NotNull;

/**
 * Klasa DTO wykorzystywana przy zwracaniu informacji o użytkowniku w punkcie końcowym typu GET
 * <code>/api/account/{login}/info</code>
 */
@Data
public class BaseAccountInfoDto implements SignableEntity {

    @NotNull
    Long version;

    @NotNull(message = "validator.incorrect.login.null")
    @Login
    private String login;

    @NotNull
    private String email;

    @NotNull
    private String name;

    @NotNull
    private String surname;


    /**
     * Konstruktor obiektu DTO użytkownika
     *
     * @param account encja użytkownika
     */
    public BaseAccountInfoDto(Account account) {
        version = account.getVersion();
        login = account.getLogin();
        email = account.getEmail();
        name = account.getName();
        surname = account.getSurname();
    }

    @Override
    public String getSignablePayload() {
        return login + version.toString();
    }
}
