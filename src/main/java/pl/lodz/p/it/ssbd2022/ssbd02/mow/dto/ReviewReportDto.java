package pl.lodz.p.it.ssbd2022.ssbd02.mow.dto;

import lombok.Data;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.ReviewReport;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class ReviewReportDto {
    @NotNull
    private final Long id;
    @NotNull
    private final Long reviewId;
    @NotNull
    private final String accountLogin;
    @NotNull
    private final String cause;
    @NotNull
    private final Boolean reviewed;
    @NotNull
    private final LocalDateTime createdAt;


    public ReviewReportDto(ReviewReport reviewReport) {
        this.id = reviewReport.getId();
        this.reviewId = reviewReport.getReview().getId();
        this.accountLogin = reviewReport.getAccount().getLogin();
        this.cause = reviewReport.getCause().getCause();
        this.reviewed = reviewReport.getReviewed();
        this.createdAt = reviewReport.getCreatedAt();
    }
}
