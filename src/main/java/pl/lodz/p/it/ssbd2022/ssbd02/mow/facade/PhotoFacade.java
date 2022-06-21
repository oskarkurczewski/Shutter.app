package pl.lodz.p.it.ssbd2022.ssbd02.mow.facade;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Photo;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.ExceptionFactory;
import pl.lodz.p.it.ssbd2022.ssbd02.util.FacadeTemplate;
import pl.lodz.p.it.ssbd2022.ssbd02.util.LoggingInterceptor;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.addPhotoToGallery;
import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.deletePhotoFromGallery;


@Stateless
@Interceptors({LoggingInterceptor.class, MowFacadeAccessInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class PhotoFacade extends FacadeTemplate<Photo> {
    @PersistenceContext(unitName = "ssbd02mowPU")
    private EntityManager em;

    public PhotoFacade() {
        super(Photo.class);
    }

    @Override
    @PermitAll
    public EntityManager getEm() {
        return em;
    }

    @Override
    @RolesAllowed(addPhotoToGallery)
    public Photo persist(Photo entity) throws BaseApplicationException {
        try {
            return super.persist(entity);
        } catch (OptimisticLockException ex) {
            throw ExceptionFactory.OptLockException();
        } catch (PersistenceException ex) {
            throw ExceptionFactory.databaseException();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ExceptionFactory.unexpectedFailException();
        }
    }

    @Override
    @DenyAll
    public Photo update(Photo entity) throws BaseApplicationException {
        try {
            return super.update(entity);
        } catch (OptimisticLockException ex) {
            throw ExceptionFactory.OptLockException();
        } catch (PersistenceException ex) {
            throw ExceptionFactory.databaseException();
        } catch (Exception ex) {
            throw ExceptionFactory.unexpectedFailException();
        }
    }

    @Override
    @RolesAllowed(deletePhotoFromGallery)
    public void remove(Photo entity) throws BaseApplicationException {
        try {
            super.remove(entity);
        } catch (OptimisticLockException ex) {
            throw ExceptionFactory.OptLockException();
        } catch (PersistenceException ex) {
            throw ExceptionFactory.databaseException();
        } catch (Exception ex) {
            throw ExceptionFactory.unexpectedFailException();
        }
    }

    @Override
    @PermitAll
    public Photo find(Long id) throws BaseApplicationException {
        try {
            return super.find(id);
        } catch (NoResultException e) {
            throw ExceptionFactory.noPhotoFoundException();
        } catch (OptimisticLockException ex) {
            throw ExceptionFactory.OptLockException();
        } catch (PersistenceException ex) {
            throw ExceptionFactory.databaseException();
        } catch (Exception ex) {
            throw ExceptionFactory.unexpectedFailException();
        }
    }


    /**
     * Metoda pozwalająca na pobieranie zdjęć fotografa
     *
     * @param photographerId konto fotografa, dla którego pobierane są zdjęcia
     * @param page           numer strony
     * @param recordsPerPage liczba recenzji na stronę
     * @return lista rezerwacji
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @PermitAll
    public List<Photo> getPhotoList(Long photographerId, int page, int recordsPerPage) throws BaseApplicationException {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Photo> query = criteriaBuilder.createQuery(Photo.class);
        Root<Photo> table = query.from(Photo.class);
        query.orderBy(criteriaBuilder.desc(table.get("id")));
        query.where(criteriaBuilder.equal(table.get("photographer").get("id"), photographerId));
        try {
            return em.createQuery(query)
                    .setFirstResult(recordsPerPage * (page - 1))
                    .setMaxResults(recordsPerPage)
                    .getResultList();
        } catch (NoResultException e) {
            throw ExceptionFactory.noAccountFound();
        } catch (OptimisticLockException ex) {
            throw ExceptionFactory.OptLockException();
        } catch (PersistenceException ex) {
            throw ExceptionFactory.databaseException();
        } catch (Exception ex) {
            throw ExceptionFactory.unexpectedFailException();
        }
    }

    /**
     * Metoda pozwalająca na pobieranie zdjęć fotografa
     *
     * @param photographerId konto fotografa, dla którego pobierane są zdjęcia
     * @return lista rezerwacji
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @PermitAll
    public Long getPhotoListSize(Long photographerId) throws BaseApplicationException {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<Photo> table = query.from(Photo.class);
        query.select(criteriaBuilder.count(table));
        query.where(criteriaBuilder.equal(table.get("photographer").get("id"), photographerId));
        try {
            return em.createQuery(query).getSingleResult();
        } catch (NoResultException e) {
            throw ExceptionFactory.noAccountFound();
        } catch (OptimisticLockException ex) {
            throw ExceptionFactory.OptLockException();
        } catch (PersistenceException ex) {
            throw ExceptionFactory.databaseException();
        } catch (Exception ex) {
            throw ExceptionFactory.unexpectedFailException();
        }
    }
}
