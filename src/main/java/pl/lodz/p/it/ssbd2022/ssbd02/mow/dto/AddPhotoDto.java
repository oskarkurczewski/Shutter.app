package pl.lodz.p.it.ssbd2022.ssbd02.mow.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AddPhotoDto {
    @NotNull
    private final String title;
    @NotNull
    private final String description;
    @NotNull
    private final byte[] data;
}
