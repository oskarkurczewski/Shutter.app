package pl.lodz.p.it.ssbd2022.ssbd02.mok.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Klasa do zmiany danych użytkownika przez Administratora
 */
@Data
public class EditAccountInfoAsAdminDto {

    @NotNull
    private String email;
    @NotNull
    private String name;
    @NotNull
    private String surname;
}