package pl.lodz.p.it.ssbd2022.ssbd02.validation.implementations;

import pl.lodz.p.it.ssbd2022.ssbd02.mor.dto.CreateReservationDto;
import pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint.Reservation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ReservationValidator implements ConstraintValidator<Reservation, CreateReservationDto> {
    @Override
    public void initialize(Reservation reservation) {

    }

    @Override
    public boolean isValid(CreateReservationDto dto, ConstraintValidatorContext ctx) {
        return dto.getFrom().isBefore(dto.getTo());
    }
}
