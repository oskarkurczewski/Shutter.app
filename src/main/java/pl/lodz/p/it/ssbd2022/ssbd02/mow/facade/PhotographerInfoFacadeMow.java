package pl.lodz.p.it.ssbd2022.ssbd02.mow.facade;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.PhotographerInfo;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.ExceptionFactory;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoPhotographerFound;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.facade.MokFacadeAccessInterceptor;
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

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.becomePhotographer;


@Stateless
@Interceptors({LoggingInterceptor.class, MokFacadeAccessInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class PhotographerInfoFacadeMow extends FacadeTemplate<PhotographerInfo> {
    @PersistenceContext(unitName = "ssbd02mowPU")
    private EntityManager em;

    public PhotographerInfoFacadeMow() {
        super(PhotographerInfo.class);
    }

    @Override
    @PermitAll
    public EntityManager getEm() {
        return em;
    }


    @Override
    @RolesAllowed(becomePhotographer)
    public PhotographerInfo update(PhotographerInfo entity) throws BaseApplicationException {
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
    @DenyAll
    public void remove(PhotographerInfo entity) throws BaseApplicationException {
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


    /**
     * Szuka profilu fotografa
     *
     * @param login Login użytkownika fotografa
     * @throws NoPhotographerFound W przypadku gdy profil fotografa dla użytkownika nie istnieje
     * @PermitAll ponieważ każdy może wyświetlić informacje o fotografie
     * @see PhotographerInfo
     */
    @PermitAll
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
