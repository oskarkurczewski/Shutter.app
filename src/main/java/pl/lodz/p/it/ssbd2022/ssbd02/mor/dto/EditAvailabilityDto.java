package pl.lodz.p.it.ssbd2022.ssbd02.mor.dto;

import lombok.Data;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.WeekDay;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Data
public class EditAvailabilityDto {
    @NotNull
    private final WeekDay day;
    @NotNull
    private final LocalTime from;
    @NotNull
    private final LocalTime to;
}
