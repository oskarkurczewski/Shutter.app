package pl.lodz.p.it.ssbd2022.ssbd02.mor.facade;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.PhotographerInfo;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.ExceptionFactory;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoPhotographerFound;
import pl.lodz.p.it.ssbd2022.ssbd02.util.FacadeTemplate;
import pl.lodz.p.it.ssbd2022.ssbd02.util.LoggingInterceptor;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.*;
import java.util.List;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.reservePhotographer;

@Stateless
@Interceptors({LoggingInterceptor.class, MorFacadeAccessInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class PhotographerFacade extends FacadeTemplate<PhotographerInfo> {
    @PersistenceContext(unitName = "ssbd02morPU")
    private EntityManager em;

    public PhotographerFacade() {
        super(PhotographerInfo.class);
    }

    @Override
    @PermitAll
    public EntityManager getEm() {
        return em;
    }


    /**
     * Metoda pozwalająca na uzyskanie stronicowanej listy wszystkich fotografów o podanej widoczności
     *
     * @param visibility     widoczność fotografa, po jakiej ma być poprowadzone wyszukiwanie
     * @param page           strona listy, którą należy pozyskać
     * @param recordsPerPage ilość krotek fotografów na stronie
     * @return stronicowana lista aktywnych fotografów obecnych systemie
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @PermitAll
    public List<PhotographerInfo> getAllPhotographersWithVisibility(
            Boolean visibility,
            int page,
            int recordsPerPage
    ) throws BaseApplicationException {
        TypedQuery<PhotographerInfo> query = getEm().createNamedQuery(
                "photographer_info.findAllWithVisibility",
                PhotographerInfo.class
        );
        query.setParameter("visibility", visibility);

        try {
            return query
                    .setFirstResult(recordsPerPage * (page - 1))
                    .setMaxResults(recordsPerPage)
                    .getResultList();
        } catch (NoResultException e) {
            throw ExceptionFactory.noPhotographerFound();
        } catch (OptimisticLockException ex) {
            throw ExceptionFactory.OptLockException();
        } catch (PersistenceException ex) {
            throw ExceptionFactory.databaseException();
        } catch (Exception ex) {
            throw ExceptionFactory.unexpectedFailException();
        }
    }

    /**
     * Metoda pozwalająca na uzyskanie liczby wszystkich aktywnych fotografów o podanej widoczności
     *
     * @param visibility widoczność fotografa, po jakiej ma być poprowadzone wyszukiwanie
     * @return liczba aktywnych fotografów obecnych systemie
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @PermitAll
    public Long countAllPhotographersWithVisibility(Boolean visibility)
            throws BaseApplicationException {
        TypedQuery<Long> query = getEm().createNamedQuery(
                "photographer_info.countAllWithVisibility",
                Long.class
        );
        query.setParameter("visibility", visibility);

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw ExceptionFactory.noPhotographerFound();
        } catch (OptimisticLockException ex) {
            throw ExceptionFactory.OptLockException();
        } catch (PersistenceException ex) {
            throw ExceptionFactory.databaseException();
        } catch (Exception ex) {
            throw ExceptionFactory.unexpectedFailException();
        }
    }

    /**
     * Szuka profilu fotografa
     *
     * @param login Login użytkownika fotografa
     * @throws NoPhotographerFound W przypadku gdy profil fotografa dla użytkownika nie istnieje
     * @see PhotographerInfo
     */
    @RolesAllowed({reservePhotographer})
    public PhotographerInfo findPhotographerByLogin(String login) throws BaseApplicationException {
        TypedQuery<PhotographerInfo> query = getEm().createNamedQuery("photographer_info.findByLogin", PhotographerInfo.class);
        query.setParameter("login", login);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw ExceptionFactory.noPhotographerFound();
        } catch (OptimisticLockException ex) {
            throw ExceptionFactory.OptLockException();
        } catch (PersistenceException ex) {
            throw ExceptionFactory.databaseException();
        } catch (Exception ex) {
            throw ExceptionFactory.unexpectedFailException();
        }
    }
}
