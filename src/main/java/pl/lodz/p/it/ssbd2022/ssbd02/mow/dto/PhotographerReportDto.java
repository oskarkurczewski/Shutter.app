package pl.lodz.p.it.ssbd2022.ssbd02.mow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class PhotographerReportDto {
    @NotNull
    private Long id;
    @NotNull
    private String accountLogin;
    @NotNull
    private String photographerLogin;
    @NotNull
    private String cause;
    @NotNull
    private Boolean reviewed;

}
