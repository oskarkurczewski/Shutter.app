package pl.lodz.p.it.ssbd2022.ssbd02.mok;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.User;
import pl.lodz.p.it.ssbd2022.ssbd02.util.FacadeTemplate;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class AuthenticationFacade extends FacadeTemplate<User> {
    @PersistenceContext(unitName = "ssbd02authPU")
    private EntityManager em;

    public AuthenticationFacade() {
        super(User.class);
    }

    @Override
    public EntityManager getEm() {
        return em;
    }

    public User findByLogin() {
        TypedQuery<User> query = getEm().createNamedQuery("User.findByLogin", User.class);
        return query.getSingleResult();
    }
}
