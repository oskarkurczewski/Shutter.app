package pl.lodz.p.it.ssbd2022.ssbd02.mow.service;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.PhotographerInfo;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Specialization;
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

import java.util.List;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.*;

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
     * Metoda aktualizująca wynik i ilość recenzji u fotografa
     * @param photographer fotograf, którego wynik i ilość recenzji zmieniamy
     * @param score wynik fotografa
     * @param reviewCount o ile zmieniona ma byc ilosc recenzji (-1, +1)
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @RolesAllowed({reviewPhotographer, deleteOwnPhotographerReview, deleteSomeonesPhotographerReview})
    public void updateScore(PhotographerInfo photographer, Long score, Long reviewCount) throws BaseApplicationException {
        photographer.setScore(photographer.getScore() + score);
        photographer.setReviewCount(photographer.getReviewCount() + reviewCount);
        facade.update(photographer);
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
    
    public void changeSpecializations(PhotographerInfo photographer, List<Specialization> newSpecializations) throws BaseApplicationException {
        photographer.setSpecializationList(newSpecializations);
        facade.update(photographer);
    }
    
    /**
     * Pobiera listę wszystkich dostępnych specjalizacji
     *
     * @return lista specjalizacji
     */
    @RolesAllowed(changeSpecializations)
    public List<Specialization> getSpecializationList() throws BaseApplicationException {
        return facade.getSpecializationList();
    }
    
   
    
}
