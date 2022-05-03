package pl.lodz.p.it.ssbd2022.ssbd02.mok.facade;

import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.hibernate.exception.ConstraintViolationException;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.User;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.DatabaseException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.IdenticalFieldException;
import pl.lodz.p.it.ssbd2022.ssbd02.util.FacadeTemplate;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;

import static pl.lodz.p.it.ssbd2022.ssbd02.util.ConstraintNames.IDENTICAL_EMAIL;
import static pl.lodz.p.it.ssbd2022.ssbd02.util.ConstraintNames.IDENTICAL_LOGIN;

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

    public void registerUser(User user) throws BaseApplicationException {
        try {
            persist(user);
        } catch (PersistenceException ex) {
            if (ex.getCause() instanceof ConstraintViolationException) {
                String name = ((ConstraintViolationException) ex.getCause()).getConstraintName();
                switch (name) {
                    case IDENTICAL_LOGIN:
                        throw new IdenticalFieldException("exception.login.identical");
                    case IDENTICAL_EMAIL:
                        throw new IdenticalFieldException("exception.email.identical");
                }
            }
            throw new DatabaseException(ex.getMessage());
        }
    }
}
