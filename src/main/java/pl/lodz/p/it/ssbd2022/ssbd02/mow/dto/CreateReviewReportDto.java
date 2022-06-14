package pl.lodz.p.it.ssbd2022.ssbd02.mow.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class CreateReviewReportDto {
    @NotNull
    private Long reviewId;
    @NotNull
    private String cause;
}
