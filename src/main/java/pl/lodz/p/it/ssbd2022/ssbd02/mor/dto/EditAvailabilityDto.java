package pl.lodz.p.it.ssbd2022.ssbd02.mor.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.WeekDay;
import pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint.Availability;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@Availability
public class EditAvailabilityDto {
    @NotNull(message = "validator.incorrect.day.null")
    private WeekDay day;
    @NotNull(message = "validator.incorrect.from.null")
    private LocalTime from;
    @NotNull(message = "validator.incorrect.to.null")
    private LocalTime to;
}
