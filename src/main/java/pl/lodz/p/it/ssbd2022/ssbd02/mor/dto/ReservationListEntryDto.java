package pl.lodz.p.it.ssbd2022.ssbd02.mor.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Reservation;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ReservationListEntryDto {

    @NotNull
    private Long id;
    @NotNull
    private LocalDateTime from;
    @NotNull
    private LocalDateTime to;
    @NotNull
    private UserDataDto client;
    @NotNull
    private UserDataDto photographer;

    public ReservationListEntryDto(Reservation reservation) {
        this.id = reservation.getId();
        this.from = reservation.getTimeFrom();
        this.to = reservation.getTimeTo();
        this.photographer = new UserDataDto(reservation.getPhotographer().getAccount());
        this.client = new UserDataDto(reservation.getAccount());
    }
}
