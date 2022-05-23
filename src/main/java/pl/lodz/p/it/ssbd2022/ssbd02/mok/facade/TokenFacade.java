package pl.lodz.p.it.ssbd2022.ssbd02.mok.facade;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.TokenType;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.VerificationToken;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.ExceptionFactory;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoVerificationTokenFound;
import pl.lodz.p.it.ssbd2022.ssbd02.util.FacadeAccessInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd02.util.FacadeTemplate;
import pl.lodz.p.it.ssbd2022.ssbd02.util.LoggingInterceptor;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Fasada obsługująca tokeny weryfikujące
 */
@Stateless
@Interceptors({LoggingInterceptor.class, FacadeAccessInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class TokenFacade extends FacadeTemplate<VerificationToken> {

    @PersistenceContext(unitName = "ssbd02mokPU")
    private EntityManager em;

    public TokenFacade() {
        super(VerificationToken.class);
    }

    @Override
    @PermitAll
    public EntityManager getEm() {
        return em;
    }

    @Override
    @PermitAll
    public VerificationToken persist(VerificationToken entity) throws BaseApplicationException {
        return super.persist(entity);
    }

    @Override
    @PermitAll
    public void remove(VerificationToken entity) throws BaseApplicationException {
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

    @PermitAll
    public VerificationToken find(String token) throws BaseApplicationException {
        TypedQuery<VerificationToken> query = getEm().createNamedQuery("VerificationToken.findByTokenEquals", VerificationToken.class);
        query.setParameter("token", token);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw ExceptionFactory.noVerificationTokenFound();
        } catch (OptimisticLockException ex) {
            throw ExceptionFactory.OptLockException();
        } catch (PersistenceException ex) {
            throw ExceptionFactory.databaseException();
        } catch (Exception ex) {
            throw ExceptionFactory.unexpectedFailException();
        }
    }

    @PermitAll
    public List<VerificationToken> findByAccountIdAndType(Account account, TokenType type) throws BaseApplicationException {
        TypedQuery<VerificationToken> query = getEm().createNamedQuery(
                "VerificationToken.findByAccountIdAndType",
                VerificationToken.class);
        query.setParameter("account", account);
        query.setParameter("type", type);
        try {
            return query.getResultList();
        } catch (OptimisticLockException ex) {
            throw ExceptionFactory.OptLockException();
        } catch (PersistenceException ex) {
            throw ExceptionFactory.databaseException();
        } catch (Exception ex) {
            throw ExceptionFactory.unexpectedFailException();
        }
    }

    @PermitAll
    public List<VerificationToken> findExpiredAfterOfType(TokenType type, LocalDateTime expiresAfter) throws BaseApplicationException {
        TypedQuery<VerificationToken> query = getEm().createNamedQuery(
                "VerificationToken.findExpiredAfterOfType",
                VerificationToken.class);
        query.setParameter("type", type);
        query.setParameter("time", expiresAfter);
        try {
            return query.getResultList();
        } catch (OptimisticLockException ex) {
            throw ExceptionFactory.OptLockException();
        } catch (PersistenceException ex) {
            throw ExceptionFactory.databaseException();
        } catch (Exception ex) {
            throw ExceptionFactory.unexpectedFailException();
        }
    }
}

