package pl.lodz.p.it.ssbd2022.ssbd02.mow.service;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.PhotographerInfo;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.*;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.facade.ProfileFacade;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.*;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class ProfileService {

    @Inject
    private ProfileFacade facade;

    @PermitAll
    public PhotographerInfo findPhotographerInfo(String login) throws BaseApplicationException {
        return facade.findByLogin(login);
    }

    /**
     * Metoda aktualizująca wynik i ilość recenzji u fotografa
     * @param photographer fotograf, którego wynik i ilość recenzji zmieniamy
     * @param score wynik fotografa
     *
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @RolesAllowed(reviewPhotographer)
    public void updateScore(PhotographerInfo photographer, Long score) throws BaseApplicationException {
        photographer.setScore(photographer.getScore() + score);
        photographer.setReviewCount(photographer.getReviewCount() + 1);
        facade.update(photographer);
    }

    /**
     * Metoda zmieniająca opis wskazanego fotografa
     * @param photographer fotograf, którego opis zmieniamy
     * @param newDescription nowy opis
     */
    @RolesAllowed(changePhotographerDescription)
    public void changeDescription(PhotographerInfo photographer, String newDescription) throws BaseApplicationException {
        photographer.setDescription(newDescription);
        facade.update(photographer);
    }
}
