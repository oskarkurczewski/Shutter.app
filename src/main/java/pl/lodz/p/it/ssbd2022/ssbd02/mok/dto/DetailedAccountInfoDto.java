package pl.lodz.p.it.ssbd2022.ssbd02.mok.dto;

import lombok.Data;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;

import javax.validation.constraints.NotNull;

/**
 * Klasa DTO wykorzystywana przy zwracaniu informacji o użytkowniku w punkcie końcowym typu GET
 * <code>/api/account/{login}/info</code>
 */
@Data
public class DetailedAccountInfoDto {

    @NotNull
    private String login;

    @NotNull
    private String email;

    @NotNull
    private String name;

    @NotNull
    private String surname;

    @NotNull
    private Boolean active;

    @NotNull
    private Boolean registered;

    /**
     * Konstruktor obiektu DTO użytkownika
     *
     * @param account encja użytkownika
     */
    public DetailedAccountInfoDto(Account account) {
        login = account.getLogin();
        email = account.getEmail();
        name = account.getName();
        surname = account.getSurname();
        active = account.getActive();
        registered = account.getRegistered();
    }

}
