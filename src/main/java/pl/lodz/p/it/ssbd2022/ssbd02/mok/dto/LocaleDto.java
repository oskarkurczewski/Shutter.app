package pl.lodz.p.it.ssbd2022.ssbd02.mok.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Klasa reprezentująca obiekt transferu danych reprezentujący preferowany jezyk użytkownika
 *
 */
@Data
public class LocaleDto {

    @pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint.Locale
    @NotNull
    private String languageTag;
}
