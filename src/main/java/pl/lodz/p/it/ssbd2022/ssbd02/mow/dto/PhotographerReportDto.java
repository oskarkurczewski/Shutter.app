package pl.lodz.p.it.ssbd2022.ssbd02.mow.dto;

import lombok.Data;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.PhotographerReport;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

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
    @NotNull
    private final LocalDateTime createdAt;

    public PhotographerReportDto(PhotographerReport photographerReport) {
        this.id = photographerReport.getId();
        this.accountLogin = photographerReport.getAccount().getLogin();
        this.photographerLogin = photographerReport.getPhotographer().getAccount().getLogin();
        this.cause = photographerReport.getCause().getCause();
        this.reviewed = photographerReport.getReviewed();
        this.createdAt = photographerReport.getCreatedAt();
    }
}
