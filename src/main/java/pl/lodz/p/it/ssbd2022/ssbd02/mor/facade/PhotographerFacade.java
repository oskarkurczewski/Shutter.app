package pl.lodz.p.it.ssbd2022.ssbd02.mor.facade;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.PhotographerInfo;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.ExceptionFactory;
import java.util.List;
import pl.lodz.p.it.ssbd2022.ssbd02.util.FacadeTemplate;
import pl.lodz.p.it.ssbd2022.ssbd02.util.LoggingInterceptor;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.*;

@Stateless
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class PhotographerFacade extends FacadeTemplate<PhotographerInfo> {
    @PersistenceContext(unitName = "ssbd02morPU")
    private EntityManager em;

    public PhotographerFacade() {
        super(PhotographerInfo.class);
    }

    /**
     * Metoda zwracająca encję zawierającą informacje o fotografie
     * @param login login fotografa
     * @return PhotographerInfo
     * @throws BaseApplicationException
     */
    @PermitAll
    public PhotographerInfo getPhotographerByLogin(String login) throws BaseApplicationException {
        TypedQuery<PhotographerInfo> query = getEm().createNamedQuery("photographer_info.findByLogin", PhotographerInfo.class);
        query.setParameter("login", login);

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            System.out.println("not found");
            throw ExceptionFactory.noAccountFound();
        } catch (OptimisticLockException ex) {
            System.out.println("opt lock");
            throw ExceptionFactory.OptLockException();
        } catch (PersistenceException ex) {
            System.out.println("persistence");
            throw ExceptionFactory.databaseException();
        } catch (Exception ex) {
            System.out.println("exception");
            throw ExceptionFactory.unexpectedFailException();
        }
    }

    @Override
    @PermitAll
    public EntityManager getEm() {
        return em;
    }


    /**
     * Metoda pozwalająca na uzyskanie stronicowanej listy wszystkich fotografów o podanej widoczności
     *
     * @param visibility widoczność fotografa, po jakiej ma być poprowadzone wyszukiwanie
     * @param page strona listy, którą należy pozyskać
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
                    .setFirstResult(recordsPerPage * (page -1))
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
}
