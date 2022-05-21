package pl.lodz.p.it.ssbd2022.ssbd02.mok.dto;

import lombok.Data;
import pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint.Password;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Klasa DTO służąca do wysłania do serwera informacji o żądaniu
 * resetu hasła użytkownika wraz z żetonem weryfikacyjnym
 */
@Data
public class ResetPasswordDto {
    @NotNull(message = "validator.incorrect.token.null")
    private String token;
    @NotNull(message = "validator.incorrect.password.null")
    @Password
    private String newPassword;
}
