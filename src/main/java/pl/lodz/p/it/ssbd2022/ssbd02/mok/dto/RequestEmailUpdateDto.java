package pl.lodz.p.it.ssbd2022.ssbd02.mok.dto;

import lombok.Getter;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint.Email;

import javax.validation.constraints.NotNull;

/**
 * Klasa DTO służąca do wysłania do serwera informacji o żądaniu
 * aktualizacji adresu email
 */
@Getter
@Setter
public class RequestEmailUpdateDto {
    @NotNull
    @Email
    String email;
}
