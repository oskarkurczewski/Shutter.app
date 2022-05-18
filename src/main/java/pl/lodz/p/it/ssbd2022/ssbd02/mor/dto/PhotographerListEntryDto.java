package pl.lodz.p.it.ssbd2022.ssbd02.mor.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class PhotographerListEntryDto {
    @NotNull
    private final String login;
    @NotNull
    private final String name;
    @NotNull
    private final String surname;
    @NotNull
    private final Long score;
    @NotNull
    private final Long reviewCount;
    @NotNull
    private final List<String> specializations;
    @NotNull
    private final Double longitude;
    @NotNull
    private final Double latitude;
}
