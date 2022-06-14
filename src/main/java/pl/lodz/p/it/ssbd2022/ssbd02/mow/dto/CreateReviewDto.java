package pl.lodz.p.it.ssbd2022.ssbd02.mow.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


/**
 * Klasa reprezentująca obiekt transferu danych potrzebnych do zrecenzowania fotografa, zawierająca jego login, ocenę
 * w formie liczby (1-10) oraz opinię słowną.
 * Na pola klasy nałożone zostały ograniczenia not null.
 */
@Data
@NoArgsConstructor
public class CreateReviewDto {
    @NotNull
    private String photographerLogin;
    @NotNull
    private Long score;
    @NotNull
    private String content;
}
