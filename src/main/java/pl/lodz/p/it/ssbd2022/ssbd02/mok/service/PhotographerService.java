package pl.lodz.p.it.ssbd2022.ssbd02.mok.service;


import pl.lodz.p.it.ssbd2022.ssbd02.entity.AccessLevelAssignment;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.PhotographerInfo;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.ExceptionFactory;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAuthenticatedAccountFound;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoPhotographerFound;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.PhotographerInfoDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.facade.AuthenticationFacade;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.facade.PhotographerInfoFacade;
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

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.*;

@Stateless
@Interceptors(LoggingInterceptor.class)
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class PhotographerService {

    @Inject
    private AuthenticationFacade accountFacade;

    @Inject
    private PhotographerInfoFacade photographerInfoFacade;


    /**
     * Odnajduje informacje o fotografie na podstawie jego loginu
     *
     * @param login Login fotografa dla którego chemy pozyskać informacje
     * @throws NoPhotographerFound W przypadku kiedy fotograf o danym loginie nie istnieje
     */
    public PhotographerInfo findByLogin(String login) throws NoPhotographerFound {
        return photographerInfoFacade.findPhotographerByLogin(login);
    }

    /**
     * Szuka fotografa
     *
     * @param requester        Konto użytkownika próbującego uzyskać informacje o danym fotografie
     * @param photographerInfo Informacje o fotografie, które próbuje pozyskać użytkownik
     * @throws NoPhotographerFound         W przypadku gdy fotograf o podanej nazwie użytkownika nie istnieje,
     *                                     gdy konto szukanego fotografa jest nieaktywne, niepotwierdzone lub
     *                                     profil nieaktywny i informacje próbuje uzyskać użytkownik
     *                                     niebędący ani administratorem, ani moderatorem
     * @see PhotographerInfoDto
     */
    @RolesAllowed({ADMINISTRATOR, MODERATOR, CLIENT, PHOTOGRAPHER})
    public PhotographerInfoDto getPhotographerInfo(Account requester, PhotographerInfo photographerInfo)
            throws NoPhotographerFound {
        List<String> accessLevelList = requester
                .getAccessLevelAssignmentList()
                .stream()
                .filter(AccessLevelAssignment::getActive)
                .map(a -> a.getLevel().getName())
                .collect(Collectors.toList());
        if (
                Boolean.FALSE.equals(photographerInfo.getAccount().getActive())
                        || Boolean.FALSE.equals(photographerInfo.getAccount().getRegistered())
                        || Boolean.FALSE.equals(photographerInfo.getVisible())
        ) {
            if (accessLevelList.contains(ADMINISTRATOR) || accessLevelList.contains(MODERATOR)) {
                return new PhotographerInfoDto(photographerInfo);
            } else {
                throw ExceptionFactory.noPhotographerFound();
            }
        } else {
            return new PhotographerInfoDto(photographerInfo);
        }

    }
}
