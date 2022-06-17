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
    private String photographerLogin;
    @NotNull
    private String clientLogin;
    @NotNull
    private LocalDateTime from;
    @NotNull
    private LocalDateTime to;

    public ReservationListEntryDto(Reservation reservation) {
        this.id = reservation.getId();
        this.photographerLogin = reservation.getPhotographer().getAccount().getLogin();
        this.clientLogin = reservation.getAccount().getLogin();
        this.from = reservation.getTimeFrom();
        this.to = reservation.getTimeTo();
    }
}
