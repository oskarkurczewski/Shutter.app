package pl.lodz.p.it.ssbd2022.ssbd02.mok.dto;

import lombok.Getter;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;

import javax.validation.constraints.NotNull;

/**
 * Klasa DTO wykorzystywana przy zwracaniu informacji o użytkowniku w punkcie końcowym typu GET
 * <code>/api/account/{login}/info</code>
 */
@Getter
@Setter
public class BaseAccountInfoDto {


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
        email = account.getEmail();
        name = account.getName();
        surname = account.getSurname();
    }

}
