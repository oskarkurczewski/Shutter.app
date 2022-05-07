package pl.lodz.p.it.ssbd2022.ssbd02.mok.service;


import pl.lodz.p.it.ssbd2022.ssbd02.entity.AccessLevelAssignment;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.PhotographerInfo;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.DataNotFoundException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAccountFound;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAuthenticatedUserFound;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.UnauthenticatedException;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.AccountInfoDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.PhotographerInfoDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.facade.AuthenticationFacade;
import pl.lodz.p.it.ssbd2022.ssbd02.security.AuthenticationContext;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Stateless
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
     * @throws DataNotFoundException W przypadku gdy fotograf o podanej nazwie użytkownika nie istnieje, 
     * gdy konto szukanego fotografa jest nieaktywne, niepotwierdzone lub profil nieaktywny i informacje prubuje uzyskać uzytkownik 
     * niebędący ani administratorem ani moderatorem
     * @throws UnauthenticatedException W przypadku gdy dane próbuje uzyskać niezalogowana osoba
     * @see PhotographerInfoDto
     */
    @RolesAllowed({"ADMINISTRATOR", "MODERATOR", "USER", "PHOTOGRAPHER"})
    public PhotographerInfoDto getPhotographerInfo(String login) throws DataNotFoundException, UnauthenticatedException {
        try {
            Account authenticatedAccount = authenticationContext.getCurrentUsersAccount();
            List<String> accesLevelList = authenticatedAccount
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
                if (accesLevelList.contains("ADMINISTRATOR") || accesLevelList.contains("MODERATOR")) {
                    return new PhotographerInfoDto(photographer);
                } else {
                    throw new DataNotFoundException("exception.photographer.notfound");
                }
            } else {
                return new PhotographerInfoDto(photographer);
            }


        } catch (NoAuthenticatedUserFound e) {
            throw new UnauthenticatedException("exception.account.unauthenticated");
        }
    }

    /**
     * Zwraca informacje o zalogowanym fotografie
     *
     * @throws DataNotFoundException W przypadku gdy profil fotografa dla użytkownika nie istnieje
     * @throws UnauthenticatedException W przypadku gdy dane próbuje uzyskać niezalogowana osoba
     * @see PhotographerInfoDto
     */
    @RolesAllowed({"PHOTOGRAPHER"})
    public PhotographerInfoDto getYourPhotographerInfo() throws UnauthenticatedException, DataNotFoundException {
        try {
            return new PhotographerInfoDto(
                    accountFacade.findPhotographerByLogin(
                            authenticationContext.getCurrentUsersAccount().getLogin()
                    )
            );
        } catch (NoAuthenticatedUserFound e) {
            throw new UnauthenticatedException("exception.account.unauthenticated");
        }
    }
}
