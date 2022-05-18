package pl.lodz.p.it.ssbd2022.ssbd02.mor.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class ReservationListEntryDto {
    @NotNull
    private final String photographerLogin;
    @NotNull
    private final String clientLogin;
    @NotNull
    private final LocalDateTime from;
    @NotNull
    private final LocalDateTime to;
}
