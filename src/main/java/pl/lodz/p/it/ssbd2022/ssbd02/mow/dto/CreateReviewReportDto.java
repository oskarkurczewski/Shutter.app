package pl.lodz.p.it.ssbd2022.ssbd02.mow.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CreateReviewReportDto {
    @NotNull
    private final Long reviewId;
    @NotNull
    private final String cause;
}
