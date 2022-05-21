package pl.lodz.p.it.ssbd2022.ssbd02.mok.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Klasa DTO służąca do wysłania do serwera informacji o żądaniu
 * resetu hasła użytkownika wraz z żetonem weryfikacyjnym
 */
@Data
public class ResetPasswordDto {
    @NotNull
    private String token;
    @NotNull
    @Size(min = 8, max = 64)
    private String newPassword;
}
