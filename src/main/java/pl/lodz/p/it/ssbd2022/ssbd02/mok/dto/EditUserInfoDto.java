package pl.lodz.p.it.ssbd2022.ssbd02.mok.dto;

import lombok.Getter;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.User;

import javax.validation.constraints.NotNull;

/**
 * Klasa do zmiany danych użytkownika
 */
@Getter
@Setter
public class EditUserInfoDto {

    @NotNull
    private String email;
    @NotNull
    private String name;
    @NotNull
    private String surname;


    public EditUserInfoDto(User user) {
        email = user.getEmail();
        name = user.getName();
        surname = user.getSurname();
    }
}