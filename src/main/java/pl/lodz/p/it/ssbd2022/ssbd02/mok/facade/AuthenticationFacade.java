package pl.lodz.p.it.ssbd2022.ssbd02.mok.facade;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.PhotographerInfo;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.User;
import pl.lodz.p.it.ssbd2022.ssbd02.util.FacadeTemplate;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AuthenticationFacade extends FacadeTemplate<User> {
    @PersistenceContext(unitName = "ssbd02mokPU")
    private EntityManager em;

    public AuthenticationFacade() {
        super(User.class);
    }

    @Override
    public EntityManager getEm() {
        return em;
    }

    public User findByLogin(String login) {
        TypedQuery<User> query = getEm().createNamedQuery("user.findByLogin", User.class);
        query.setParameter("login", login);
        return query.getSingleResult();
    }
    
    public PhotographerInfo findPhotographerByLogin(String login) {
        TypedQuery<PhotographerInfo> query = getEm().createNamedQuery("photographer_info.findByLogin", PhotographerInfo.class);
        query.setParameter("login", login);
        return query.getSingleResult();
    }
}
