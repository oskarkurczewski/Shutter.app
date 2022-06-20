package pl.lodz.p.it.ssbd2022.ssbd02.mok.dto;

import lombok.Data;
import pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint.Email;
import pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint.Token;

import javax.validation.constraints.NotNull;

/**
 * Klasa DTO służąca do potwierdzenia aktualizacji adresu emailu wraz z żetonem weryfikacyjnym
 */
@Data
public class EmailUpdateDto {
    @NotNull
    @Email
    String newEmail;
    @NotNull
    @Token
    private String token;
}
