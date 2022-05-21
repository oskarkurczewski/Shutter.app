package pl.lodz.p.it.ssbd2022.ssbd02.mok.dto;

import lombok.Getter;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint.Email;

import javax.validation.constraints.NotNull;

/**
 * Klasa DTO służąca do potwierdzenia aktualizacji adresu emailu wraz z żetonem weryfikacyjnym
 */
@Getter
@Setter
public class EmailUpdateDto {
    @NotNull
    @Email
    String newEmail;
    @NotNull
    private String token;
}
