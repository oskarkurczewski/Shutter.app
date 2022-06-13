package pl.lodz.p.it.ssbd2022.ssbd02.mor.dto;

import lombok.Data;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Availability;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.WeekDay;

import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Data
public class AvailabilityDto {
    @NotNull
    private WeekDay day;
    @NotNull
    private LocalTime from;
    @NotNull
    private LocalTime to;

    public AvailabilityDto(Availability availability) {
        this.day = availability.getDay();
        this.from = availability.getFrom();
        this.to = availability.getTo();
    }
}
