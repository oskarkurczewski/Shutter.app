package pl.lodz.p.it.ssbd2022.ssbd02.mor.service;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Availability;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.PhotographerInfo;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.AvailabilityOverlapException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAvailabilityFoundException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoPhotographerFoundException;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.dto.AvailabilityDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.dto.EditAvailabilityDto;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.naming.OperationNotSupportedException;
import java.util.List;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.changeAvailabilityHours;


@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AvailabilityService {

    @PermitAll
    public Availability findById(Long id) throws NoAvailabilityFoundException {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(changeAvailabilityHours)
    public void addAvailability(Availability newAvailability) throws AvailabilityOverlapException {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(changeAvailabilityHours)
    public void removeAvailability(Availability availability) {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(changeAvailabilityHours)
    public void editAvailability(Availability availability, EditAvailabilityDto availabilityEdit) {
        throw new UnsupportedOperationException();
    }

    @PermitAll
    public List<Availability> listAvailabilities(PhotographerInfo photographer) {
        throw new UnsupportedOperationException();
    }
}
