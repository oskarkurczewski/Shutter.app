package pl.lodz.p.it.ssbd2022.ssbd02.mor.facade;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Availability;
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
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import java.util.List;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.*;

@Stateless
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AvailabilityFacade extends FacadeTemplate<Availability> {
    @PersistenceContext(unitName = "ssbd02morPU")
    private EntityManager em;

    public AvailabilityFacade() {
        super(Availability.class);
    }


    @Override
    @RolesAllowed(changeAvailabilityHours)
    public Availability persist(Availability entity) throws BaseApplicationException {
        try {
            return super.persist(entity);
        } catch (OptimisticLockException ex) {
            throw ExceptionFactory.OptLockException();
        } catch (PersistenceException ex) {
            throw ExceptionFactory.databaseException();
        } catch (Exception ex) {
            throw ExceptionFactory.unexpectedFailException();
        }
    }

    @Override
    @RolesAllowed(changeAvailabilityHours)
    public Availability update(Availability entity) throws BaseApplicationException {
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
    @RolesAllowed(changeAvailabilityHours)
    public void remove(Availability entity) throws BaseApplicationException {
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

    @RolesAllowed(changeAvailabilityHours)
    public void removeAll(List<Availability> availabilities) throws BaseApplicationException {
        try {
            EntityManager em = this.getEm();
            for (Availability availability : availabilities) {
                em.remove(availability);
            }
            em.flush();
        } catch (OptimisticLockException ex) {
            throw ExceptionFactory.OptLockException();
        } catch (PersistenceException ex) {
            throw ExceptionFactory.databaseException();
        } catch (Exception ex) {
            throw ExceptionFactory.unexpectedFailException();
        }
    }

    @RolesAllowed(changeAvailabilityHours)
    public void addAll(List<Availability> availabilities) throws BaseApplicationException {
        try {
            EntityManager em = this.getEm();
            for (Availability availability : availabilities) {
                em.persist(availability);
            }
            em.flush();
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
    public EntityManager getEm() {
        return em;
    }
}
