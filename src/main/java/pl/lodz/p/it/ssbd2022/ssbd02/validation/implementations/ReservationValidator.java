package pl.lodz.p.it.ssbd2022.ssbd02.validation.implementations;

import pl.lodz.p.it.ssbd2022.ssbd02.mor.dto.CreateReservationDto;
import pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint.Availability;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ReservationValidator implements ConstraintValidator<Availability, CreateReservationDto> {
    @Override
    public void initialize(Availability availability) {

    }

    @Override
    public boolean isValid(CreateReservationDto dto, ConstraintValidatorContext ctx) {
        return dto.getFrom().isBefore(dto.getTo());
    }
}
