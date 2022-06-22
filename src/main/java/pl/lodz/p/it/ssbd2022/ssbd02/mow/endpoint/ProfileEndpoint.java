package pl.lodz.p.it.ssbd2022.ssbd02.mow.endpoint;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.PhotographerInfo;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Specialization;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.ExceptionFactory;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.dto.ChangeDescriptionDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.service.ProfileService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.changePhotographerDescription;
import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.changeSpecializations;

@Stateful
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class ProfileEndpoint extends AbstractEndpoint {

    @Inject
    private ProfileService profileService;

    @Inject
    private AuthenticationContext authCtx;

    /**
     * Metoda zmieniająca opis zalogowanego fotografa
     *
     * @param newDescription obiekt zawierający nowy opis spełniający warunki poprawności
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @RolesAllowed(changePhotographerDescription)
    public void changeDescription(ChangeDescriptionDto newDescription) throws BaseApplicationException {
        String caller = authCtx.getCurrentUsersLogin();
        PhotographerInfo pInfo = profileService.findPhotographerInfo(caller);
        String newDescString = Optional.ofNullable(newDescription.getContent()).orElse("");
        profileService.changeDescription(pInfo, newDescString);
    }

    /**
     * Zmienia listę specjalizacji dla zalogowanego fotografa
     *
     * @param newSpecializations lista nowych specjalizacji
     * @throws BaseApplicationException w przypadku wystąpienia błędu
     */
    @RolesAllowed(changeSpecializations)
    public void changeSpecializations(List<String> newSpecializations) throws BaseApplicationException {

        List<Specialization> specializations = profileService.getSpecializationList();
        List<String> specializationsConverted = specializations.stream().map(Specialization::getName).collect(Collectors.toList());

        List<Specialization> newSpecializationsConverted = new ArrayList<>();

        try {
            newSpecializations.forEach(newSpecialization -> {
                if (specializationsConverted.contains(newSpecialization)) {
                    newSpecializationsConverted.add(specializations.stream().filter(specialization -> specialization.getName().equals(newSpecialization)).findFirst().get());
                } else {
                    throw new IllegalArgumentException();
                }
            });
        } catch (IllegalArgumentException e) {
            throw ExceptionFactory.specializationNotFoundException();
        }

        String caller = authCtx.getCurrentUsersLogin();
        PhotographerInfo photographerInfo = profileService.findPhotographerInfo(caller);
        profileService.changeSpecializations(photographerInfo, newSpecializationsConverted);
    }

    /**
     * Zwraca listę specjalizacji dla zalogowanego fotografa
     *
     * @return lista specjalizacji
     * @throws BaseApplicationException w przypadku wystąpienia błędu
     */
    @RolesAllowed(changeSpecializations)
    public List<String> getOwnSpecializations() throws BaseApplicationException {
        String caller = authCtx.getCurrentUsersLogin();
        PhotographerInfo photographerInfo = profileService.findPhotographerInfo(caller);
        return photographerInfo.getSpecializationList().stream().map(Specialization::getName).collect(Collectors.toList());
    }

    /**
     * Zwraca wszystkie dostepne specjalizacje
     *
     * @return lista specjalizacji
     * @throws BaseApplicationException w przypadku wystąpienia błędu
     */
    @PermitAll
    public List<String> getAllSpecializations() throws BaseApplicationException {
        List<Specialization> specializations = profileService.getSpecializationList();
        return specializations.stream().map(Specialization::getName).collect(Collectors.toList());
    }
}
