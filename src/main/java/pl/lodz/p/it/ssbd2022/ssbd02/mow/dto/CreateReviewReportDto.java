package pl.lodz.p.it.ssbd2022.ssbd02.mow.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class CreateReviewReportDto {
    @NotNull(message = "validator.incorrect.id.null")
    private Long reviewId;

    @NotNull(message = "validator.incorrect.cause.null")
    private String cause;
}
