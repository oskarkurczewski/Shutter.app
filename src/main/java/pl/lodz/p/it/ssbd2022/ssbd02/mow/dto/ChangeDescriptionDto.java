package pl.lodz.p.it.ssbd2022.ssbd02.mow.dto;

import lombok.Getter;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint.Description;

@Getter
@Setter
public class ChangeDescriptionDto {
    @Description
    private String content;
}
