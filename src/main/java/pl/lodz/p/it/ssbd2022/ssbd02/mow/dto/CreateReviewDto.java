package pl.lodz.p.it.ssbd2022.ssbd02.mow.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class CreateReviewDto {
    @NotNull
    private String photographerLogin;
    @NotNull
    private Long score;
    @NotNull
    private String content;
}
