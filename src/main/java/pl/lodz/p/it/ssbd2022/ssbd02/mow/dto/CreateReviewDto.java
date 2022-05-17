package pl.lodz.p.it.ssbd2022.ssbd02.mow.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CreateReviewDto {
    @NotNull
    private final String photographerLogin;
    @NotNull
    private final String clientLogin;
}
