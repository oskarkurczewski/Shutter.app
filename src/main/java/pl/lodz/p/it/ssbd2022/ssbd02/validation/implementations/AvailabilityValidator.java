package pl.lodz.p.it.ssbd2022.ssbd02.validation.implementations;

import pl.lodz.p.it.ssbd2022.ssbd02.mor.dto.EditAvailabilityDto;
import pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint.Availability;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AvailabilityValidator implements ConstraintValidator<Availability, EditAvailabilityDto> {

    @Override
    public void initialize(Availability availability) {

    }

    @Override
    public boolean isValid(EditAvailabilityDto dto, ConstraintValidatorContext ctx) {
        return dto.getFrom().isBefore(dto.getTo());
    }
}
