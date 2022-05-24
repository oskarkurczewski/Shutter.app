package pl.lodz.p.it.ssbd2022.ssbd02.mor.endpoint;

import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.AvailabilityOverlapException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAuthenticatedAccountFound;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAvailabilityFoundException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoPhotographerFoundException;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.dto.AvailabilityDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.dto.CreateAvailabilityDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.dto.EditAvailabilityDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.service.AvailabilityService;
import pl.lodz.p.it.ssbd2022.ssbd02.util.AbstractEndpoint;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.List;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.changeAvailabilityHours;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class AvailabilityEndpoint extends AbstractEndpoint {

    @Inject
    private AvailabilityService availabilityService;

    @RolesAllowed(changeAvailabilityHours)
    public void addAvailability(CreateAvailabilityDto createAvailabilityDto)
            throws AvailabilityOverlapException, NoAuthenticatedAccountFound {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(changeAvailabilityHours)
    public void removeAvailability(Long availabilityId)
            throws NoAvailabilityFoundException, NoAuthenticatedAccountFound {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(changeAvailabilityHours)
    public void editAvailability(Long availabilityId, EditAvailabilityDto availabilityEdit)
            throws NoAvailabilityFoundException, NoAuthenticatedAccountFound {
        throw new UnsupportedOperationException();
    }

    @PermitAll
    public List<AvailabilityDto> listAvailabilities(String photographerLogin) throws NoPhotographerFoundException {
        throw new UnsupportedOperationException();
    }
}
