package pl.lodz.p.it.ssbd2022.ssbd02.mok.facade;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.TokenType;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.VerificationToken;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.ExceptionFactory;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoVerificationTokenFound;
import pl.lodz.p.it.ssbd2022.ssbd02.util.FacadeTemplate;
import pl.lodz.p.it.ssbd2022.ssbd02.util.LoggingInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd02.util.FacadeAccessInterceptor;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
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
    public EntityManager getEm() {
        return em;
    }

    public VerificationToken find(String token) throws NoVerificationTokenFound {
        TypedQuery<VerificationToken> query = getEm().createNamedQuery("VerificationToken.findByTokenEquals", VerificationToken.class);
        query.setParameter("token", token);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw ExceptionFactory.noVerificationTokenFound();
        }
    }

    public List<VerificationToken> findByAccountIdAndType(Account account, TokenType type) {
        TypedQuery<VerificationToken> query = getEm().createNamedQuery(
                "VerificationToken.findByAccountIdAndType",
                VerificationToken.class);
        query.setParameter("account", account);
        query.setParameter("type", type);
        return query.getResultList();
    }

    public List<VerificationToken> findExpiredAfterOfType(TokenType type, LocalDateTime expiresAfter) {
        TypedQuery<VerificationToken> query = getEm().createNamedQuery(
                "VerificationToken.findExpiredAfterOfType",
                VerificationToken.class);
        query.setParameter("type", type);
        query.setParameter("time", expiresAfter);
        return query.getResultList();
    }
}

