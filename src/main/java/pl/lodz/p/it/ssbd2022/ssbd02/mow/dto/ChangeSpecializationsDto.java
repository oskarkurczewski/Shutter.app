package pl.lodz.p.it.ssbd2022.ssbd02.mow.dto;

import lombok.Data;

import java.util.List;

@Data
public class ChangeSpecializationsDto {
    private List<String> addSpecializations;
    private List<String> removeSpecializations;
}
