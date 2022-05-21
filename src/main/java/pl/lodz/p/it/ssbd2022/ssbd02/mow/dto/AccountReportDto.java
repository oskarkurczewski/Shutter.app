package pl.lodz.p.it.ssbd2022.ssbd02.mow.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AccountReportDto {
    @NotNull
    private final Long id;
    @NotNull
    private final String reportedLogin;
    @NotNull
    private final String reporteeLogin;
    @NotNull
    private final String cause;
    @NotNull
    private final Boolean reviewed;
}
