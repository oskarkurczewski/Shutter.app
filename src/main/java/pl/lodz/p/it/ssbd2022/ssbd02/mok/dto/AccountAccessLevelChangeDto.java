package pl.lodz.p.it.ssbd2022.ssbd02.mok.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint.AccessLevel;

import javax.validation.constraints.NotNull;

/**
 * Klasa reprezentująca obiekt transferu danych zawierający informację o poziomie dostępu.
 * Na pola klasy nałożone zostały ograniczenia.
 */
@Getter
@Setter
@NoArgsConstructor
public class AccountAccessLevelChangeDto {
    @NotNull(message = "validator.incorrect.access_level.null")
    @AccessLevel
    private String accessLevel;

    @NotNull(message = "validator.incorrect.active.null")
    private Boolean active;

    public AccountAccessLevelChangeDto(String accessLevel, boolean active) {
        this.accessLevel = accessLevel;
        this.active = active;
    }
}
