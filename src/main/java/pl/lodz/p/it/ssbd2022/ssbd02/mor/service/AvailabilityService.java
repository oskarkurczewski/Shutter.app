package pl.lodz.p.it.ssbd2022.ssbd02.mor.service;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Availability;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.PhotographerInfo;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.*;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.facade.AvailabilityFacade;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.List;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.changeAvailabilityHours;


@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AvailabilityService {

    @Inject
    AvailabilityFacade availabilityFacade;

    /**
     * Metoda nadpisująca przedziały dostępności fotografa. Poprzednie zakresy zastępowane są tymi, podanymi przez parametr
     *
     * @param availabilities lista nowych przedziałów dostępności
     * @param photographer   fotograf, dla którego należy nadpisać przedziały dostępności
     * @throws NoAuthenticatedAccountFound  akcja wykonywana przez niezalogowanego użytkownika
     * @throws NoPhotographerFoundException nie znaleziono fotografa o podanym loginie
     */
    @RolesAllowed(changeAvailabilityHours)
    public void editAvailability(List<Availability> availabilities, PhotographerInfo photographer) throws BaseApplicationException {
        availabilityFacade.removeAll(photographer.getAvailability());
        availabilityFacade.addAll(availabilities);
    }
}
