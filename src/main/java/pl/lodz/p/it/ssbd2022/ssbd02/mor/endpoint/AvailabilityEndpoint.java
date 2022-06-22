package pl.lodz.p.it.ssbd2022.ssbd02.mor.endpoint;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Availability;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.PhotographerInfo;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAuthenticatedAccountFound;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoPhotographerFound;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoPhotographerFoundException;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.dto.AvailabilityDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.dto.EditAvailabilityDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.service.AvailabilityService;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.service.PhotographerService;
import pl.lodz.p.it.ssbd2022.ssbd02.security.AuthenticationContext;
import pl.lodz.p.it.ssbd2022.ssbd02.util.AbstractEndpoint;
import pl.lodz.p.it.ssbd2022.ssbd02.util.LoggingInterceptor;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.changeAvailabilityHours;

@Stateful
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class AvailabilityEndpoint extends AbstractEndpoint {

    @Inject
    private AuthenticationContext authenticationContext;

    @Inject
    private AvailabilityService availabilityService;

    @Inject
    private PhotographerService photographerService;

    /**
     * Metoda nadpisująca przedziały dostępności fotografa. Poprzednie zakresy zastępowane są tymi, podanymi przez parametr
     *
     * @param availabilitiesDto lista nowych przedziałów dostępności
     * @throws NoAuthenticatedAccountFound  akcja wykonywana przez niezalogowanego użytkownika
     * @throws NoPhotographerFoundException nie znaleziono fotografa o podanym loginie
     */
    @RolesAllowed(changeAvailabilityHours)
    public void editAvailability(List<EditAvailabilityDto> availabilitiesDto) throws BaseApplicationException {
        PhotographerInfo photographer = photographerService.getPhotographer(authenticationContext.getCurrentUsersLogin());

        List<Availability> availabilities = new ArrayList<>();
        for (EditAvailabilityDto availabilityDto : availabilitiesDto) {
            Availability availability = new Availability();
            availability.setPhotographer(photographer);
            availability.setDay(availabilityDto.getDay());
            availability.setFrom(availabilityDto.getFrom());

            LocalTime toFixed = availabilityDto.getTo();
            if (toFixed.equals(LocalTime.MIDNIGHT)) {
                toFixed = LocalTime.of(23, 59, 59);
            }
            availability.setTo(toFixed);

            availabilities.add(availability);
        }

        availabilityService.editAvailability(availabilities, photographer);
    }

    /**
     * Metoda zwracająca listę godzin dostępności dla podanego fotografa
     *
     * @param photographerLogin login fotografa
     * @return AvailabilityDto lista godzin dostępności
     * @throws NoPhotographerFound nie znaleziono fotografa o podanym loginie
     */
    @PermitAll
    public List<AvailabilityDto> listAvailabilities(String photographerLogin) throws NoPhotographerFound {
        List<Availability> availabilities = photographerService.getPhotographer(photographerLogin).getAvailability();

        List<AvailabilityDto> availabilityDtoList = new ArrayList<>();
        for (Availability availability : availabilities) {
            availabilityDtoList.add(new AvailabilityDto(availability));
        }

        return availabilityDtoList;
    }
}
