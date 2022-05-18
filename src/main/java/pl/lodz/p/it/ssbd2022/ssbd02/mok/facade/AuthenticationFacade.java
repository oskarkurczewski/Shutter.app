package pl.lodz.p.it.ssbd2022.ssbd02.mok.facade;

import org.hibernate.exception.ConstraintViolationException;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.AccessLevelAssignment;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.AccessLevelValue;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.PhotographerInfo;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.*;
import pl.lodz.p.it.ssbd2022.ssbd02.util.FacadeTemplate;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static pl.lodz.p.it.ssbd2022.ssbd02.util.ConstraintNames.IDENTICAL_EMAIL;
import static pl.lodz.p.it.ssbd2022.ssbd02.util.ConstraintNames.IDENTICAL_LOGIN;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AuthenticationFacade extends FacadeTemplate<Account> {
    @PersistenceContext(unitName = "ssbd02mokPU")
    private EntityManager em;

    public AuthenticationFacade() {
        super(Account.class);
    }

    @Override
    public EntityManager getEm() {
        return em;
    }

    public Account findByLogin(String login) throws NoAccountFound {
        TypedQuery<Account> query = getEm().createNamedQuery("account.findByLogin", Account.class);
        query.setParameter("login", login);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw ExceptionFactory.noAccountFound();
        }
    }

    /**
     * Pobiera przypisanie poziomu dostępu z bazy danych na podstawie przekazanego łańcucha znaków
     * dla wskazanego użytkownika.
     *
     * @param account Konto użytkownika, dla którego wyszukiwany jest poziom dostępu
     * @param accessLevelValue Wartość poziomu dostępu, który chcemy wyszukać
     * @return null w przypadku, gdy funkcja nie znajdzie poszukiwanego poziomu dostępu
     */
    public AccessLevelAssignment getAccessLevelAssignmentForAccount(Account account, AccessLevelValue accessLevelValue) {
        return account.getAccessLevelAssignmentList().stream()
                .filter(a -> a.getLevel().getName().equals(accessLevelValue.getName()))
                .findAny()
                .orElse(null);
    }

    /**
     * Pobiera poziom dostępu z bazy danych na podstawie przekazanego łańcucha znaków,
     * w przypadku nieznalezienia pasującego wyniku otrzymujemy wyjątek
     *
     * @param accessLevel łańcuch znaków zawierający nazwę poziomu dostępu
     * @throws DataNotFoundException W przypadku, gdy funkcja nie znajdzie rekordu
     * ze wskazaną nazwą
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
     * Szuka profilu fotografa
     *
     * @param login nazwa użytkownika fotografa
     * @throws NoPhotographerFound W przypadku gdy fotograf o podanej nazwie użytkownika nie istnieje
     * @see PhotographerInfo
     */
    public PhotographerInfo findPhotographerByLogin(String login) throws NoPhotographerFound {
        TypedQuery<PhotographerInfo> query = getEm().createNamedQuery("photographer_info.findByLogin", PhotographerInfo.class);
        query.setParameter("login", login);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw ExceptionFactory.noPhotographerFound();
        }
    }

    /**
     * Tworzy konto użytkownika w bazie danych,
     * w przypadku naruszenia unikatowości loginu lub adresu email otrzymujemy wyjątek
     *
     * @param account obiekt encji użytkownika
     * @throws IdenticalFieldException W przypadku, gdy login lub adres email już się znajduje w bazie danych
     */
    public void registerAccount(Account account) throws DatabaseException, IdenticalFieldException {
        try {
            persist(account);
        } catch (PersistenceException ex) {
            if (ex.getCause() instanceof ConstraintViolationException) {
                String name = ((ConstraintViolationException) ex.getCause()).getConstraintName();
                switch (name) {
                    case IDENTICAL_LOGIN:
                        throw ExceptionFactory.identicalFieldException("exception.login.identical");
                    case IDENTICAL_EMAIL:
                        throw ExceptionFactory.identicalFieldException("exception.email.identical");
                }
            }
            throw ExceptionFactory.databaseException();
        }
    }

    public List<Account> getAccountList(int page, int recordsPerPage, String orderBy, String order) {
        List<Account> accountList = new ArrayList<>();
        switch (order) {
            case "asc": {
                accountList = getEm()
                        .createNamedQuery("account.getAccountListAsc", Account.class)
                        .setParameter("column", orderBy)
                        .setFirstResult(recordsPerPage * (page - 1))
                        .setMaxResults(recordsPerPage)
                        .getResultList();
                break;
            }
            case "desc": {
                accountList = getEm()
                        .createNamedQuery("account.getAccountListDesc", Account.class)
                        .setParameter("column", orderBy)
                        .setFirstResult(recordsPerPage * (page - 1))
                        .setMaxResults(recordsPerPage)
                        .getResultList();
                break;
            }
        }
        return accountList;
    }
    
    public int getAccountTableSize() {
        return getEm()
                .createNamedQuery("account.getTableSize", Account.class)
                .getFirstResult();
    }
}
