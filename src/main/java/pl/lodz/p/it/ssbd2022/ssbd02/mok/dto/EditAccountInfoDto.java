package pl.lodz.p.it.ssbd2022.ssbd02.mok.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;

import javax.validation.constraints.NotNull;

/**
 * Klasa do zmiany danych użytkownika
 */
@Getter
@Setter
@NoArgsConstructor
public class EditAccountInfoDto {

    @NotNull
    private String email;
    @NotNull
    private String name;
    @NotNull
    private String surname;


    public EditAccountInfoDto(Account account) {
        email = account.getEmail();
        name = account.getName();
        surname = account.getSurname();
    }
}