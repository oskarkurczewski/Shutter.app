package pl.lodz.p.it.ssbd2022.ssbd02.mok.endpoint;

import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAuthenticatedAccountFound;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoPhotographerFound;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.PhotographerInfoDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.service.PhotographerService;
import pl.lodz.p.it.ssbd2022.ssbd02.util.LoggingInterceptor;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

@Stateful
@Interceptors(LoggingInterceptor.class)
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class PhotographerEndpoint {

    @Inject
    private PhotographerService photographerService;

    /**
     * Szuka fotografa
     *
     * @param login nazwa użytkownika fotografa
     * @throws NoPhotographerFound         W przypadku gdy fotograf o podanej nazwie użytkownika nie istnieje,
     *                                     gdy konto szukanego fotografa jest nieaktywne, niepotwierdzone lub profil nieaktywny i informacje próbuje uzyskać użytkownik
     *                                     niebędący ani administratorem, ani moderatorem
     * @throws NoAuthenticatedAccountFound W przypadku gdy dane próbuje uzyskać niezalogowana osoba
     * @see PhotographerInfoDto
     */
    @RolesAllowed({"ADMINISTRATOR", "MODERATOR", "CLIENT", "PHOTOGRAPHER"})
    public PhotographerInfoDto getPhotographerInfo(String login) throws NoPhotographerFound, NoAuthenticatedAccountFound {
        return photographerService.getPhotographerInfo(login);
    }

    /**
     * Zwraca informacje o zalogowanym fotografie
     *
     * @throws NoPhotographerFound         W przypadku gdy profil fotografa dla użytkownika nie istnieje
     * @throws NoAuthenticatedAccountFound W przypadku gdy dane próbuje uzyskać niezalogowana osoba
     * @see PhotographerInfoDto
     */
    @RolesAllowed({"PHOTOGRAPHER"})
    public PhotographerInfoDto getYourPhotographerInfo() throws NoPhotographerFound, NoAuthenticatedAccountFound {
        return photographerService.getYourPhotographerInfo();
    }
}
