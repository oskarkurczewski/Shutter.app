package pl.lodz.p.it.ssbd2022.ssbd02.mok.dto;

import lombok.Data;
import pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint.ReCaptchaToken;

import javax.validation.constraints.NotNull;

/**
 * Klasa DTO służąca do wysłania do serwera informacji o żądaniu
 * wysłania prośby o reset hasła
 */
@Data
public class RecaptchaTokenDto {
    @NotNull(message = "validator.incorrect.re_captcha.null")
    @ReCaptchaToken
    private String reCaptchaToken;
}


