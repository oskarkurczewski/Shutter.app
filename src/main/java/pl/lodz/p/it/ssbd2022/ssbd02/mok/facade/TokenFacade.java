package pl.lodz.p.it.ssbd2022.ssbd02.mok.facade;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.VerificationToken;
import pl.lodz.p.it.ssbd2022.ssbd02.util.FacadeTemplate;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * Fasada obsługująca tokeny weryfikujące
 */
@Stateless
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

    public VerificationToken find(String token) {
        TypedQuery<VerificationToken> query = getEm().createNamedQuery("VerificationToken.findByTokenEquals", VerificationToken.class);
        query.setParameter("token", token);
        return query.getSingleResult();
    }
}
