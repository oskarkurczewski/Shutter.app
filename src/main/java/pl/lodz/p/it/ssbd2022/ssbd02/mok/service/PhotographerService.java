package pl.lodz.p.it.ssbd2022.ssbd02.mok.service;


import pl.lodz.p.it.ssbd2022.ssbd02.entity.AccessLevelAssignment;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.PhotographerInfo;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.ExceptionFactory;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAuthenticatedAccountFound;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoPhotographerFound;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.PhotographerInfoDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.facade.AuthenticationFacade;
import pl.lodz.p.it.ssbd2022.ssbd02.security.AuthenticationContext;
import pl.lodz.p.it.ssbd2022.ssbd02.util.LoggingInterceptor;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
@Interceptors(LoggingInterceptor.class)
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class PhotographerService {

    @Inject
    private AuthenticationFacade accountFacade;
    @Inject
    private AuthenticationContext authenticationContext;


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

        Account authenticatedAccount = authenticationContext.getCurrentUsersAccount();
        List<String> accessLevelList = authenticatedAccount
                .getAccessLevelAssignmentList()
                .stream()
                .filter(AccessLevelAssignment::getActive)
                .map(a -> a.getLevel().getName())
                .collect(Collectors.toList());
        PhotographerInfo photographer = accountFacade.findPhotographerByLogin(login);
        if (
                Boolean.FALSE.equals(photographer.getAccount().getActive())
                        || Boolean.FALSE.equals(photographer.getAccount().getRegistered())
                        || Boolean.FALSE.equals(photographer.getVisible())
        ) {
            if (accessLevelList.contains("ADMINISTRATOR") || accessLevelList.contains("MODERATOR")) {
                return new PhotographerInfoDto(photographer);
            } else {
                throw ExceptionFactory.noPhotographerFound();
            }
        } else {
            return new PhotographerInfoDto(photographer);
        }

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
        return new PhotographerInfoDto(
                accountFacade.findPhotographerByLogin(
                        authenticationContext.getCurrentUsersAccount().getLogin()
                )
        );
    }
}
