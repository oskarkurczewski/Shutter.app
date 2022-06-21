package pl.lodz.p.it.ssbd2022.ssbd02.mow.endpoint;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.PhotographerInfo;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.ExceptionFactory;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAuthenticatedAccountFound;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoPhotographerFound;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.service.AccountService;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.dto.BasePhotographerInfoDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.dto.DetailedPhotographerInfoDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.service.PhotographerService;
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

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.getEnhancedPhotographerInfo;
import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.getOwnPhotographerInfo;

@Stateful
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class PhotographerEndpoint extends AbstractEndpoint {

    @Inject
    private PhotographerService photographerService;

    @Inject
    private AccountService accountService;

    @Inject
    private AuthenticationContext authenticationContext;

    /**
     * Szuka fotografa
     *
     * @param login Login użytkownika fotografa
     * @return podstawowy zbiór informacji o fotografie
     * @throws NoPhotographerFound         W przypadku gdy fotograf o podanej nazwie użytkownika nie istnieje,
     *                                     gdy konto szukanego fotografa jest nieaktywne, niepotwierdzone lub
     *                                     profil nieaktywny i informacje próbuje uzyskać użytkownik
     *                                     niebędący ani administratorem, ani moderatorem
     * @throws NoAuthenticatedAccountFound W przypadku gdy dane próbuje uzyskać niezalogowana osoba
     * @see BasePhotographerInfoDto
     */
    @PermitAll
    public BasePhotographerInfoDto getPhotographerInfo(String login) throws BaseApplicationException {
        PhotographerInfo photographerInfo = photographerService.findByLogin(login);
        if (photographerInfo.getVisible()) {
            return new BasePhotographerInfoDto(photographerInfo);
        }
        throw ExceptionFactory.noPhotographerFound();
    }

    /**
     * Szuka fotografa
     *
     * @param login Login użytkownika fotografa
     * @return rozszerzony zbiór informacji o fotografie
     * @throws NoPhotographerFound         W przypadku gdy fotograf o podanej nazwie użytkownika nie istnieje,
     *                                     gdy konto szukanego fotografa jest nieaktywne, niepotwierdzone lub
     *                                     profil nieaktywny i informacje próbuje uzyskać użytkownik
     *                                     niebędący ani administratorem, ani moderatorem
     * @throws NoAuthenticatedAccountFound W przypadku gdy dane próbuje uzyskać niezalogowana osoba
     * @see BasePhotographerInfoDto
     */
    @RolesAllowed(getEnhancedPhotographerInfo)
    public DetailedPhotographerInfoDto getEnhancedPhotographerInfo(String login) throws BaseApplicationException {
        PhotographerInfo photographerInfo = photographerService.findByLogin(login);
        return new DetailedPhotographerInfoDto(photographerService.getPhotographerInfo(photographerInfo));
    }

    /**
     * Zwraca informacje o zalogowanym fotografie
     *
     * @return rozszerzony zbiór informacji o danym fotografie
     * @throws NoPhotographerFound         W przypadku gdy profil fotografa dla użytkownika nie istnieje
     * @throws NoAuthenticatedAccountFound W przypadku gdy dane próbuje uzyskać niezalogowana osoba
     * @see BasePhotographerInfoDto
     */
    @RolesAllowed(getOwnPhotographerInfo)
    public DetailedPhotographerInfoDto getYourPhotographerInfo() throws BaseApplicationException {
        Account account = accountService.findByLogin(authenticationContext.getCurrentUsersLogin());
        return new DetailedPhotographerInfoDto(photographerService.findByLogin(account.getLogin()));
    }
}
