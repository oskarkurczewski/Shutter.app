package pl.lodz.p.it.ssbd2022.ssbd02.mow.facade;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.PhotographerInfo;
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

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.changePhotographerDescription;
import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.getPhotographerInfo;

@Stateless
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class ProfileFacade extends FacadeTemplate<PhotographerInfo> {
    @PersistenceContext(unitName = "ssbd02mowPU")
    private EntityManager em;

    public ProfileFacade() {
        super(PhotographerInfo.class);
    }

    @Override
    @RolesAllowed(changePhotographerDescription)
    public PhotographerInfo update(PhotographerInfo entity) throws BaseApplicationException {
        try {
            return super.update(entity);
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

    @Override
    @RolesAllowed(getPhotographerInfo)
    public PhotographerInfo find(Long id) throws BaseApplicationException {
        try {
            return super.find(id);
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

    @Override
    protected EntityManager getEm() {
        return em;
    }

    @PermitAll
    public PhotographerInfo findByLogin(String login) throws BaseApplicationException {
        TypedQuery<PhotographerInfo> query = getEm().createNamedQuery("photographer_info.findByLogin", PhotographerInfo.class);
        query.setParameter("login", login);
        try {
            return query.getSingleResult();
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
