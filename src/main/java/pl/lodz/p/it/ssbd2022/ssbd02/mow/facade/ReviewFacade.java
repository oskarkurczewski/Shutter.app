package pl.lodz.p.it.ssbd2022.ssbd02.mow.facade;

import org.hibernate.exception.ConstraintViolationException;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Review;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.ExceptionFactory;
import pl.lodz.p.it.ssbd2022.ssbd02.util.FacadeTemplate;
import pl.lodz.p.it.ssbd2022.ssbd02.util.LoggingInterceptor;

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

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.*;
import static pl.lodz.p.it.ssbd2022.ssbd02.util.ConstraintNames.IDENTICAL_ID;

@Stateless
@Interceptors({LoggingInterceptor.class, MowFacadeAccessInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class ReviewFacade extends FacadeTemplate<Review> {
    @PersistenceContext(unitName = "ssbd02mowPU")
    private EntityManager em;

    public ReviewFacade() {
        super(Review.class);
    }

    @Override
    @PermitAll
    public EntityManager getEm() {
        return em;
    }

    @Override
    public Review find(Long id) throws BaseApplicationException {
        try {
            return super.find(id);
        } catch (NoResultException e) {
            throw ExceptionFactory.noReviewFoundException();
        } catch (OptimisticLockException ex) {
            throw ExceptionFactory.OptLockException();
        } catch (PersistenceException ex) {
            throw ExceptionFactory.databaseException();
        } catch (Exception ex) {
            throw ExceptionFactory.unexpectedFailException();
        }
    }

    /**
     * Rejestruje stworzenie encji JPA recenzji fotografa
     *
     * @param entity obiekt recenzji
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @Override
    @RolesAllowed(reviewPhotographer)
    public Review persist(Review entity) throws BaseApplicationException {
        try {
            return super.persist(entity);
        } catch (OptimisticLockException ex) {
            throw ExceptionFactory.OptLockException();
        } catch (PersistenceException ex) {
            if (ex.getCause() instanceof ConstraintViolationException) {
                String name = ((ConstraintViolationException) ex.getCause()).getConstraintName();
                if (IDENTICAL_ID.equals(name)) {
                    throw ExceptionFactory.identicalFieldException("exception.review_already_exists");
                }
            }

            throw ExceptionFactory.databaseException();
        } catch (Exception ex) {
            throw ExceptionFactory.unexpectedFailException();
        }
    }

    @Override
    @RolesAllowed({deleteOwnPhotographerReview, deleteSomeonesPhotographerReview})
    public Review update(Review entity) throws BaseApplicationException {
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

    @PermitAll
    public List<Review> getReviewListByPhotographer(
            int page,
            int recordsPerPage,
            Long photographerId
    ) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Review> query = criteriaBuilder.createQuery(Review.class);
        Root<Review> table = query.from(Review.class);
        query.select(table);
        query.where(
                criteriaBuilder.and(
                        criteriaBuilder.equal(table.get("photographer").get("id"), photographerId),
                        criteriaBuilder.equal(table.get("active"), true)
                )
        );

        return em
                .createQuery(query)
                .setFirstResult(recordsPerPage * (page - 1))
                .setMaxResults(recordsPerPage)
                .getResultList();
    }

    @PermitAll
    public Long getReviewListSize(Long photographerId) throws BaseApplicationException {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<Review> table = query.from(Review.class);
        query.select(criteriaBuilder.count(table));
        query.where(
                criteriaBuilder.and(
                        criteriaBuilder.equal(table.get("photographer").get("id"), photographerId),
                        criteriaBuilder.equal(table.get("active"), true)
                )
        );
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
