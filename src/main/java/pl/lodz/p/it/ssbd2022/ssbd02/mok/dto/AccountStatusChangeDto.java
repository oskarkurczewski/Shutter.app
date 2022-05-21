package pl.lodz.p.it.ssbd2022.ssbd02.mok.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;


/**
 * Klasa reprezentująca zmianę statusu użytkownika w systemie
 */
@Data
public class AccountStatusChangeDto {

    @NotNull
    private Boolean active;

}
