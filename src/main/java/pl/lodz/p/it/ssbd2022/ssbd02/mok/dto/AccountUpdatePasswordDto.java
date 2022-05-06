package pl.lodz.p.it.ssbd2022.ssbd02.mok.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Klasa DTO służąca do wysłania do serwera informacji o żądaniu
 * zmiany hasła użytkownika o wskazanym id na wskazane w polu password
 */
@Getter
@Setter
public class AccountUpdatePasswordDto {
    @NotNull
    @Size(min = 8, max = 64)
    private String password;
    @Size(min = 8, max = 64)
    private String oldPassword;
}
