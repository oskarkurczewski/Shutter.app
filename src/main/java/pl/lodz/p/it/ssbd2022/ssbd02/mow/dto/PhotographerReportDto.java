package pl.lodz.p.it.ssbd2022.ssbd02.mow.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PhotographerReportDto {
    @NotNull
    private final Long id;
    @NotNull
    private final String accountLogin;
    @NotNull
    private final String photographerLogin;
    @NotNull
    private final String cause;
    @NotNull
    private final Boolean reviewed;
}
