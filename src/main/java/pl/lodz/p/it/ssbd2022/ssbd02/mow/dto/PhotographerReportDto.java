package pl.lodz.p.it.ssbd2022.ssbd02.mow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.PhotographerReport;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

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
    @NotNull
    private LocalDateTime createdAt;

    public PhotographerReportDto(PhotographerReport photographerReport) {
        this.id = photographerReport.getId();
        this.accountLogin = photographerReport.getAccount().getLogin();
        this.photographerLogin = photographerReport.getPhotographer().getAccount().getLogin();
        this.cause = photographerReport.getCause().getCause();
        this.reviewed = photographerReport.getReviewed();
        this.createdAt = photographerReport.getCreatedAt();
    }
}
