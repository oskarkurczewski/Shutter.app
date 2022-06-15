package pl.lodz.p.it.ssbd2022.ssbd02.mow.endpoint;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.PhotographerInfo;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Specialization;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.dto.ChangeDescriptionDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.dto.ChangeSpecializationsDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.service.ProfileService;
import pl.lodz.p.it.ssbd2022.ssbd02.security.AuthenticationContext;
import pl.lodz.p.it.ssbd2022.ssbd02.util.AbstractEndpoint;
import pl.lodz.p.it.ssbd2022.ssbd02.util.LoggingInterceptor;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.util.List;
import java.util.Optional;

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
     */
    @RolesAllowed(changePhotographerDescription)
    public void changeDescription(ChangeDescriptionDto newDescription) throws BaseApplicationException {
        String caller = authCtx.getCurrentUsersLogin();
        PhotographerInfo pInfo = profileService.findPhotographerInfo(caller);
        String newDescString = Optional.ofNullable(newDescription.getContent()).orElse("");
        profileService.changeDescription(pInfo, newDescString);
    }

    /**
     * Zmienia specjalizacje zalogowanego fotografa
     *
     * @param changeSpecializationsDto obiekt dto zawierający specjalizacje do dodania i/lub usunięcia
     * @throws BaseApplicationException operacja sie nie udała
     * @see ChangeSpecializationsDto
     */
    @RolesAllowed(changeSpecializations)
    public void changeSpecializations(ChangeSpecializationsDto changeSpecializationsDto) throws BaseApplicationException {
        profileService.changeSpecializations(changeSpecializationsDto);
    }

    /**
     * Pobiera listę wszystkich dostępnych specjalizacji
     *
     * @return lista specjalizacji
     */
    @RolesAllowed(changeSpecializations)
    public List<String> getSpecializationList() {
        return profileService
                .getSpecializationList()
                .stream()
                .map(Specialization::getName)
                .collect(java.util.stream.Collectors.toList());
    }
}
