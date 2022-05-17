package pl.lodz.p.it.ssbd2022.ssbd02.mor.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TimePeriodDto {
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
}
