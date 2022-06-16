package pl.lodz.p.it.ssbd2022.ssbd02.mow.dto;

import lombok.Data;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.AccountReport;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

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
    @NotNull
    private final LocalDateTime createdAt;

    public AccountReportDto(AccountReport accountReport) {
        this.id = accountReport.getId();
        this.reportedLogin = accountReport.getReported().getLogin();
        this.reporteeLogin = accountReport.getReportee().getLogin();
        this.cause = accountReport.getCause().getCause();
        this.reviewed = accountReport.getReviewed();
        this.createdAt = accountReport.getCreatedAt();
    }
}
