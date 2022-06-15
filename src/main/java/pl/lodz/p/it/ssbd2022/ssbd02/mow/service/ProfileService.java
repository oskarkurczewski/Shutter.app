package pl.lodz.p.it.ssbd2022.ssbd02.mow.service;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.PhotographerInfo;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.facade.ProfileFacade;
import pl.lodz.p.it.ssbd2022.ssbd02.util.LoggingInterceptor;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.changePhotographerDescription;

@Stateless
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class ProfileService {

    @Inject
    private ProfileFacade facade;

    @PermitAll
    public PhotographerInfo findPhotographerInfo(String login) throws BaseApplicationException {
        return facade.findByLogin(login);
    }

    /**
     * Metoda zmieniająca opis wskazanego fotografa
     *
     * @param photographer   fotograf, którego opis zmieniamy
     * @param newDescription nowy opis
     */
    @RolesAllowed(changePhotographerDescription)
    public void changeDescription(PhotographerInfo photographer, String newDescription) throws BaseApplicationException {
        photographer.setDescription(newDescription);
        facade.update(photographer);
    }
}
