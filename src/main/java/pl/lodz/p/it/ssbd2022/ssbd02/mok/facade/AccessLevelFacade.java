package pl.lodz.p.it.ssbd2022.ssbd02.mok.facade;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.AccessLevelAssignment;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.AccessLevelValue;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.DataNotFoundException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.ExceptionFactory;
import pl.lodz.p.it.ssbd2022.ssbd02.util.FacadeAccessInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd02.util.FacadeTemplate;
import pl.lodz.p.it.ssbd2022.ssbd02.util.LoggingInterceptor;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.*;
import java.util.List;

/**
 * Fasada obsługująca tokeny weryfikujące
 */
@Stateless
@Interceptors({LoggingInterceptor.class, FacadeAccessInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AccessLevelFacade extends FacadeTemplate<AccessLevelAssignment> {

    @PersistenceContext(unitName = "ssbd02mokPU")
    private EntityManager em;

    public AccessLevelFacade() {
        super(AccessLevelAssignment.class);
    }

    @Override
    public AccessLevelAssignment persist(AccessLevelAssignment entity) throws BaseApplicationException {
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
    public void remove(AccessLevelAssignment entity) throws BaseApplicationException {
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

    @Override
    public AccessLevelAssignment update(AccessLevelAssignment entity) throws BaseApplicationException {
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
    public EntityManager getEm() {
        return em;
    }

    /**
     * Pobiera poziom dostępu z bazy danych na podstawie przekazanego łańcucha znaków,
     * w przypadku nieznalezienia pasującego wyniku otrzymujemy wyjątek
     *
     * @param accessLevel łańcuch znaków zawierający nazwę poziomu dostępu
     * @throws DataNotFoundException W przypadku, gdy funkcja nie znajdzie rekordu
     *                               ze wskazaną nazwą
     */
    public AccessLevelValue getAccessLevelValue(String accessLevel) throws DataNotFoundException {
        TypedQuery<AccessLevelValue> query = getEm().createNamedQuery("account.getAccessLevelValue", AccessLevelValue.class);
        query.setParameter("access_level", accessLevel);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw new DataNotFoundException("exception.access_level.not_found");
        }

    }

    /**
     * Pobiera przypisanie poziomu dostępu z bazy danych na podstawie przekazanego łańcucha znaków
     * dla wskazanego użytkownika.
     *
     * @param account          Konto użytkownika, dla którego wyszukiwany jest poziom dostępu
     * @param accessLevelValue Wartość poziomu dostępu, który chcemy wyszukać
     * @return null w przypadku, gdy funkcja nie znajdzie poszukiwanego poziomu dostępu
     */
    public AccessLevelAssignment getAccessLevelAssignmentForAccount(Account account, AccessLevelValue accessLevelValue) {
        return account.getAccessLevelAssignmentList().stream()
                .filter(a -> a.getLevel().getName().equals(accessLevelValue.getName()))
                .findAny()
                .orElse(null);
    }
}

