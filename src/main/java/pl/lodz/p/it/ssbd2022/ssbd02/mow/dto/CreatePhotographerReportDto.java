package pl.lodz.p.it.ssbd2022.ssbd02.mow.dto;

import lombok.Data;
import pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint.Cause;
import pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint.Login;

import javax.validation.constraints.NotNull;

@Data
public class CreatePhotographerReportDto {
    @NotNull(message = "validator.incorrect.photographer.null")
    @Login
    private String photographerLogin;

    @NotNull(message = "validator.incorrect.cause.null")
    @Cause
    private String cause;
}
