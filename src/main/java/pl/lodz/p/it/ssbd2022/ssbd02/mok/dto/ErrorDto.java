package pl.lodz.p.it.ssbd2022.ssbd02.mok.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 *  Klasa DTO wykorzystywana przy zwracaniu do użytkownika informacji o błędzie. Zawiera ona informację o błędzie
 *  oraz typ mówiący o tym, z czym powiązany jest dany błąd.
 *
 */
@Data
@AllArgsConstructor
public class ErrorDto {

    @NotNull
    private String message;
}
