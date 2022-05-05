package pl.lodz.p.it.ssbd2022.ssbd02.mok.facade;

import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.hibernate.exception.ConstraintViolationException;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.AccessLevelValue;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.PhotographerInfo;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.User;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.CustomApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAuthenticatedUser;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.DatabaseException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.IdenticalFieldException;
import pl.lodz.p.it.ssbd2022.ssbd02.util.FacadeTemplate;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
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

    public User findByLogin(String login) throws NoAuthenticatedUser {
        TypedQuery<User> query = getEm().createNamedQuery("user.findByLogin", User.class);
        query.setParameter("login", login);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw CustomApplicationException.NoAuthenticatedUser();
        }
    }

    public AccessLevelValue getAccessLevelValue(String accessLevel) {
        TypedQuery<AccessLevelValue> query = getEm().createNamedQuery("user.getAccessLevelValue", AccessLevelValue.class);
        query.setParameter("access_level", accessLevel);
        return query.getSingleResult();
    }
    
    public PhotographerInfo findPhotographerByLogin(String login) {
        TypedQuery<PhotographerInfo> query = getEm().createNamedQuery("photographer_info.findByLogin", PhotographerInfo.class);
        query.setParameter("login", login);
        return query.getSingleResult();
    }

    /**
     * Tworzy konto użytkownika w bazie danych,
     * w przypadku naruszenia unikatowości loginu lub adresu email otrzymujemy wyjątek
     *
     * @param user obiekt encji użytkownika
     * @throws BaseApplicationException W przypadku, gdy login lub adres email już się znajduje w bazie danych
     */
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
            //  TODO jakaś wiadomość do wyjątku?
            throw new DatabaseException(ex.getMessage());
        }
    }
}
