package pl.lodz.p.it.ssbd2022.ssbd02.mok.endpoint;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.PhotographerInfo;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAuthenticatedAccountFound;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoPhotographerFound;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.PhotographerInfoDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.service.PhotographerService;
import pl.lodz.p.it.ssbd2022.ssbd02.security.AuthenticationContext;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.*;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class PhotographerEndpoint {

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
     * @see PhotographerInfoDto
     */
    @RolesAllowed({ADMINISTRATOR, MODERATOR, CLIENT, PHOTOGRAPHER})
    public PhotographerInfoDto getPhotographerInfo(String login) throws NoPhotographerFound, NoAuthenticatedAccountFound {
        Account requester = authenticationContext.getCurrentUsersAccount();
        PhotographerInfo photographerInfo = photographerService.findByLogin(login);
        return photographerService.getPhotographerInfo(requester, photographerInfo);
    }

    /**
     * Zwraca informacje o zalogowanym fotografie
     *
     * @throws NoPhotographerFound         W przypadku gdy profil fotografa dla użytkownika nie istnieje
     * @throws NoAuthenticatedAccountFound W przypadku gdy dane próbuje uzyskać niezalogowana osoba
     * @see PhotographerInfoDto
     */
    @RolesAllowed({PHOTOGRAPHER})
    public PhotographerInfoDto getYourPhotographerInfo() throws NoPhotographerFound, NoAuthenticatedAccountFound {
        Account account = authenticationContext.getCurrentUsersAccount();
        return new PhotographerInfoDto(photographerService.findByLogin(account.getLogin()));
    }
}
