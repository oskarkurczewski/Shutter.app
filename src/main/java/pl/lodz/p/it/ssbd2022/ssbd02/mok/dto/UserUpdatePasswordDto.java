package pl.lodz.p.it.ssbd2022.ssbd02.mok.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Klasa DTO służąca do wysłania do serwera informacji o żądaniu
 * zmiany hasła użytkownika o wskazanym id na wskazane w polu password
 */
@Getter
@Setter
public class UserUpdatePasswordDto {
    private Long id;
    private String password;
}
