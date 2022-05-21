package pl.lodz.p.it.ssbd2022.ssbd02.mok.endpoint;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.PhotographerInfo;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAuthenticatedAccountFound;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoPhotographerFound;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.BasePhotographerInfoDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.DetailedPhotographerInfoDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.service.PhotographerService;
import pl.lodz.p.it.ssbd2022.ssbd02.security.AuthenticationContext;
import pl.lodz.p.it.ssbd2022.ssbd02.util.AbstractEndpoint;
import pl.lodz.p.it.ssbd2022.ssbd02.util.LoggingInterceptor;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.*;

@Stateful
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class PhotographerEndpoint extends AbstractEndpoint {

    @Inject
    private PhotographerService photographerService;

    @Inject
    private AuthenticationContext authenticationContext;

    /**
     * Szuka fotografa
     *
     * @param login nazwa użytkownika fotografa
     * @throws NoPhotographerFound         W przypadku gdy fotograf o podanej nazwie użytkownika nie istnieje,
     *                                     gdy konto szukanego fotografa jest nieaktywne, niepotwierdzone lub
     *                                     profil nieaktywny i informacje próbuje uzyskać użytkownik
     *                                     niebędący ani administratorem, ani moderatorem
     * @throws NoAuthenticatedAccountFound W przypadku gdy dane próbuje uzyskać niezalogowana osoba
     * @see BasePhotographerInfoDto
     */
    @RolesAllowed(getPhotographerInfo)
    public BasePhotographerInfoDto getPhotographerInfo(String login) throws NoPhotographerFound, NoAuthenticatedAccountFound {
        PhotographerInfo photographerInfo = photographerService.findByLogin(login);
        return new BasePhotographerInfoDto(photographerService.getPhotographerInfo(photographerInfo));
    }
    /**
     * Szuka fotografa
     *
     * @param login nazwa użytkownika fotografa
     * @throws NoPhotographerFound         W przypadku gdy fotograf o podanej nazwie użytkownika nie istnieje,
     *                                     gdy konto szukanego fotografa jest nieaktywne, niepotwierdzone lub
     *                                     profil nieaktywny i informacje próbuje uzyskać użytkownik
     *                                     niebędący ani administratorem, ani moderatorem
     * @throws NoAuthenticatedAccountFound W przypadku gdy dane próbuje uzyskać niezalogowana osoba
     * @see BasePhotographerInfoDto
     */
    @RolesAllowed(getEnhancedPhotographerInfo)
    public DetailedPhotographerInfoDto getEnhancedPhotographerInfo(String login) throws NoPhotographerFound, NoAuthenticatedAccountFound {
        PhotographerInfo photographerInfo = photographerService.findByLogin(login);
        return new DetailedPhotographerInfoDto(photographerService.getPhotographerInfo(photographerInfo));
    }

    /**
     * Zwraca informacje o zalogowanym fotografie
     *
     * @throws NoPhotographerFound         W przypadku gdy profil fotografa dla użytkownika nie istnieje
     * @throws NoAuthenticatedAccountFound W przypadku gdy dane próbuje uzyskać niezalogowana osoba
     * @see BasePhotographerInfoDto
     */
    @RolesAllowed(getOwnPhotographerInfo)
    public DetailedPhotographerInfoDto getYourPhotographerInfo() throws NoPhotographerFound, NoAuthenticatedAccountFound {
        Account account = authenticationContext.getCurrentUsersAccount();
        return new DetailedPhotographerInfoDto(photographerService.findByLogin(account.getLogin()));
    }
}
