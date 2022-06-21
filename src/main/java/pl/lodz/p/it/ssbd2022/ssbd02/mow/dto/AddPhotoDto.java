package pl.lodz.p.it.ssbd2022.ssbd02.mow.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint.PhotoDescription;
import pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint.PhotoTitle;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class AddPhotoDto {
    @NotNull(message = "validator.incorrect.photo_title.null")
    @PhotoTitle
    private String title;
    @NotNull(message = "validator.incorrect.photo_description.null")
    @PhotoDescription
    private String description;
    @NotNull
    private String data;
}
