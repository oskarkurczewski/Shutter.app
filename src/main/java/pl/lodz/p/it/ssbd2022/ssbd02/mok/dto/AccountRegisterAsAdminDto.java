package pl.lodz.p.it.ssbd2022.ssbd02.mok.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * Klasa reprezentująca rozszerzony obiekt tranferu danych reprezentujący wymagane dane użytkownika
 * służące do utworzenia konta użytkownika (zawiera dodatkowe pola: registered oraz active).
 * Na pola klasy nałożone zostały ograniczenia.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AccountRegisterAsAdminDto extends AccountRegisterDto {

    @NotNull(message = "validator.incorrect.registered.null")
    private Boolean registered;

    @NotNull(message = "validator.incorrect.active.null")
    private Boolean active;

}
