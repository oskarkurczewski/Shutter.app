package pl.lodz.p.it.ssbd2022.ssbd02.mow.service;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.PhotographerInfo;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Specialization;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.ExceptionFactory;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.dto.ChangeSpecializationsDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.facade.ProfileFacade;
import pl.lodz.p.it.ssbd2022.ssbd02.security.AuthenticationContext;
import pl.lodz.p.it.ssbd2022.ssbd02.util.LoggingInterceptor;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.util.ArrayList;
import java.util.List;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.*;

/**
 * The type Profile service.
 */
@Stateless
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class ProfileService {

    @Inject
    private ProfileFacade profileFacade;

    @Inject
    private AuthenticationContext authenticationContext;

    @PermitAll
    public PhotographerInfo findPhotographerInfo(String login) throws BaseApplicationException {
        return profileFacade.findByLogin(login);
    }

    /**
     * Metoda aktualizująca wynik i ilość recenzji u fotografa
     *
     * @param photographer fotograf, którego wynik i ilość recenzji zmieniamy
     * @param score        wynik fotografa
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @RolesAllowed(reviewPhotographer)
    public void updateScore(PhotographerInfo photographer, Long score) throws BaseApplicationException {
        photographer.setScore(photographer.getScore() + score);
        photographer.setReviewCount(photographer.getReviewCount() + 1);
        profileFacade.update(photographer);
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
        profileFacade.update(photographer);
    }


    /**
     * Zmienia specjalizacje zalogowanego fotografa
     *
     * @param changeSpecializationsDto obiekt dto zawierający specjalizacje do dodania i/lub usunięcia
     * @throws BaseApplicationException operacja sie nie udała
     * @see ChangeSpecializationsDto
     */
    @RolesAllowed(changeSpecializations)
    public void changeSpecializations(ChangeSpecializationsDto changeSpecializationsDto) throws BaseApplicationException {
        PhotographerInfo photographer = profileFacade.findByLogin(authenticationContext.getCurrentUsersLogin());
        List<Specialization> allSpecializations = profileFacade.getSpecializationList();

        if (changeSpecializationsDto.getAddSpecializations() != null && changeSpecializationsDto.getRemoveSpecializations() != null) {
            List<String> temp = new ArrayList<>(changeSpecializationsDto.getAddSpecializations());
            temp.retainAll(changeSpecializationsDto.getRemoveSpecializations());
            if (!temp.isEmpty()) {
                throw ExceptionFactory.specializationRepeatedException();
            }
        }

        if (changeSpecializationsDto.getAddSpecializations() != null) {
            if (checkSpecializationsNonExistance(changeSpecializationsDto.getAddSpecializations(), allSpecializations)) {
                throw ExceptionFactory.specializationNotFoundException();
            }
            if (checkSpecializationsExistance(changeSpecializationsDto.getAddSpecializations(), photographer.getSpecializationList())) {
                throw ExceptionFactory.photographerAlreadySpecializedException();
            }
            changeSpecializationsDto.getAddSpecializations().forEach(
                    specialization -> photographer.getSpecializationList().add(
                            allSpecializations.stream().filter(
                                    s -> s.getName().equals(specialization)
                            ).findFirst().get()
                    )
            );
        }

        if (changeSpecializationsDto.getRemoveSpecializations() != null) {
            if (checkSpecializationsNonExistance(changeSpecializationsDto.getRemoveSpecializations(), allSpecializations)) {
                throw ExceptionFactory.specializationNotFoundException();
            }
            if (checkSpecializationsNonExistance(changeSpecializationsDto.getRemoveSpecializations(), photographer.getSpecializationList())) {
                throw ExceptionFactory.photographerNotSpecializedException();
            }

            changeSpecializationsDto.getRemoveSpecializations().forEach(
                    specialization -> photographer.getSpecializationList().remove(
                            allSpecializations.stream().filter(
                                    s -> s.getName().equals(specialization)
                            ).findFirst().get()
                    )
            );
        }


        profileFacade.update(photographer);
    }

    /**
     * Sprawdza, czy na liście kontrolnej znajduje się jakikolwiek element z podanej listy
     *
     * @param specializations lista do sprawdzenia
     * @param controlList     lista kontrolna
     * @return true, jeśli lista kontrolna zawiera jakikolwiek element z podanej listy
     */
    @RolesAllowed(changeSpecializations)
    private boolean checkSpecializationsExistance
    (List<String> specializations, List<Specialization> controlList) {
        for (String specialization : specializations) {

            if (controlList.stream().anyMatch(s -> s.getName().equals(specialization))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sprawdza, czy lista kontrolna zawiera wszystkie elementy z podanej listy
     *
     * @param specializations lista do sprawdzenia
     * @param controlList     lista kontrolna
     * @return true, jeśli przynajmniej jeden element z podanej listy nie znajduje się w liście kontrolnej
     */
    @RolesAllowed(changeSpecializations)
    private boolean checkSpecializationsNonExistance
    (List<String> specializations, List<Specialization> controlList) {
        for (String specialization : specializations) {
            if (controlList.stream().noneMatch(s -> s.getName().equals(specialization))) {
                return true;
            }
        }
        return false;
    }


    /**
     * Pobiera listę wszystkich dostępnych specjalizacji
     *
     * @return lista specjalizacji
     */
    @RolesAllowed(changeSpecializations)
    public List<Specialization> getSpecializationList() {
        return profileFacade.getSpecializationList();
    }


}
