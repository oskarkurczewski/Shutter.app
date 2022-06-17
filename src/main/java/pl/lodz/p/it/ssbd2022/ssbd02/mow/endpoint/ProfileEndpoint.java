package pl.lodz.p.it.ssbd2022.ssbd02.mow.endpoint;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.PhotographerInfo;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.dto.ChangeDescriptionDto;
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
import java.util.Optional;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.changePhotographerDescription;

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
}
