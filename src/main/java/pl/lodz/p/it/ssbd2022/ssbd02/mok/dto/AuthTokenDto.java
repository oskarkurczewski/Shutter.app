package pl.lodz.p.it.ssbd2022.ssbd02.mok.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Klasa DTO służąca do wysłania do użytkownika żetonu JWT
 */
@Data
@AllArgsConstructor
public class AuthTokenDto {

    @NotNull
    private String token;
}

