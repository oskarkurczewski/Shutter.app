package pl.lodz.p.it.ssbd2022.ssbd02.mor.service;


import pl.lodz.p.it.ssbd2022.ssbd02.entity.PhotographerInfo;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.ExceptionFactory;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoPhotographerFound;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoPhotographerFoundException;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.facade.PhotographerFacade;
import pl.lodz.p.it.ssbd2022.ssbd02.util.LoggingInterceptor;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

@Stateless
@Interceptors(LoggingInterceptor.class)
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class PhotographerService {

    @Inject
    PhotographerFacade photographerFacade;

    /**
     * Metoda zwracająca encję zawierającą informacje o fotografie
     *
     * @param login login fotografa
     * @return PhotographerInfo
     * @throws NoPhotographerFoundException nie znaleziono fotografa o podanym loginie
     */
    @PermitAll
    public PhotographerInfo getPhotographer(String login) throws NoPhotographerFound {
        try {
            return photographerFacade.getPhotographerByLogin(login);
        } catch (BaseApplicationException e) {
            throw ExceptionFactory.noPhotographerFound();
        }
    }
}
