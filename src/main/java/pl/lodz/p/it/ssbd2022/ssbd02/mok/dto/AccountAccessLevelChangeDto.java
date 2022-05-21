package pl.lodz.p.it.ssbd2022.ssbd02.mok.dto;

import lombok.Data;
import pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint.AccessLevel;

import javax.validation.constraints.NotNull;

/**
 * Klasa reprezentująca obiekt transferu danych zawierający informację o poziomie dostępu.
 * Na pola klasy nałożone zostały ograniczenia.
 */
@Data
public class AccountAccessLevelChangeDto {
    @NotNull(message = "validator.incorrect.access_level.null")
    @AccessLevel
    private final String accessLevel;

    @NotNull(message = "validator.incorrect.active.null")
    private final Boolean active;
}
