package pl.lodz.p.it.ssbd2022.ssbd02.mok.facade;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.AccountListPreferences;
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

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.listAllAccounts;

@Stateless
@Interceptors({LoggingInterceptor.class, MokFacadeAccessInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AccountListPreferencesFacade extends FacadeTemplate<AccountListPreferences> {

    @PersistenceContext(unitName = "ssbd02mokPU")
    private EntityManager em;

    public AccountListPreferencesFacade() {
        super(AccountListPreferences.class);
    }

    @RolesAllowed(listAllAccounts)
    public AccountListPreferences findByAccount(Account account) throws BaseApplicationException {
        TypedQuery<AccountListPreferences> query = getEm().createNamedQuery(
                "accountListPreferences.findByAccount",
                AccountListPreferences.class
        );
        query.setParameter("account", account);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw ExceptionFactory.noAccountListPreferencesFound();
        } catch (OptimisticLockException ex) {
            throw ExceptionFactory.OptLockException();
        } catch (PersistenceException ex) {
            throw ExceptionFactory.databaseException();
        } catch (Exception ex) {
            throw ExceptionFactory.unexpectedFailException();
        }
    }

    @Override
    @RolesAllowed(listAllAccounts)
    public AccountListPreferences persist(AccountListPreferences entity) throws BaseApplicationException {
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
    @RolesAllowed(listAllAccounts)
    public AccountListPreferences update(AccountListPreferences entity) throws BaseApplicationException {
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
    @PermitAll
    public EntityManager getEm() {
        return em;
    }
}
