package pl.lodz.p.it.ssbd2022.ssbd02.mok.dto;

import lombok.Data;
import pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint.Password;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Klasa DTO służąca do wysłania do serwera informacji o żądaniu
 * zmiany hasła użytkownika o wskazanym id na wskazane w polu password
 */
@Data
public class AccountUpdatePasswordDto {
    @NotNull
    @Password
    private String password;
    @Password
    private String oldPassword;
}
