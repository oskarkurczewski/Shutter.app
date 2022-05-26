package pl.lodz.p.it.ssbd2022.ssbd02.mok.facade;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.ExceptionFactory;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.WrongParameterException;
import pl.lodz.p.it.ssbd2022.ssbd02.util.FacadeTemplate;
import pl.lodz.p.it.ssbd2022.ssbd02.util.LoggingInterceptor;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;


@Stateless
@Interceptors({LoggingInterceptor.class, MokFacadeAccessInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AuthenticationFacade extends FacadeTemplate<Account> {
    @PersistenceContext(unitName = "ssbd02mokPU")
    private EntityManager em;

    public AuthenticationFacade() {
        super(Account.class);
    }

    /**
     * dodaje znak '%' na początku i na końcu struny
     *
     * @param s struna
     * @return struna wynikowa
     */
    private String addPercent(String s) {
        return "%" + s + "%";
    }

    @Override
    @PermitAll
    public Account persist(Account entity) throws BaseApplicationException {
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
    @PermitAll
    public Account update(Account entity) throws BaseApplicationException {
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
    public void remove(Account entity) throws BaseApplicationException {
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
    public Account findByLogin(String login) throws BaseApplicationException {
        TypedQuery<Account> query = getEm().createNamedQuery("account.findByLogin", Account.class);
        query.setParameter("login", login);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            throw ExceptionFactory.noAccountFound();
        } catch (OptimisticLockException ex) {
            throw ExceptionFactory.OptLockException();
        } catch (PersistenceException ex) {
            throw ExceptionFactory.databaseException();
        } catch (Exception ex) {
            throw ExceptionFactory.unexpectedFailException();
        }
    }

    /**
     * Zwraca listę wszystkich użytkowników których imię lub nazwisko pasuje do podanej frazy
     *
     * @param name           fraza zawierająca się w imieniu/nazwisku
     * @param page           numer strony do pobrania
     * @param recordsPerPage liczba rekordów na stronie
     * @param orderBy        nazwa kolumny, po której nastąpi sortowanie
     * @param order          kolejność sortowania
     * @return lista wynikowa zapytania do bazy danych
     * @throws WrongParameterException zła nazwa kolumny
     */
    @PermitAll
    public List<String> findByNameSurname(String name, int page, int recordsPerPage, String orderBy, String order) throws WrongParameterException {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<String> query = criteriaBuilder.createQuery(String.class);
        Root<Account> table = query.from(Account.class);
        query.select(table.get("login"));

        try {
            switch (order) {
                case "asc": {
                    query.orderBy(criteriaBuilder.asc(table.get(orderBy)));
                    break;

                }
                case "desc": {
                    query.orderBy(criteriaBuilder.desc(table.get(orderBy)));
                    break;

                }
            }
        } catch (IllegalArgumentException e) {
            throw ExceptionFactory.wrongParameterException();
        }

        query.where(
                criteriaBuilder.or(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(table.get("name")), addPercent(name.toLowerCase())
                        ),
                        criteriaBuilder.like(
                                criteriaBuilder.lower(table.get("surname")), addPercent(name.toLowerCase())
                        )
                )
        );

        return em
                .createQuery(query)
                .setFirstResult(recordsPerPage * (page - 1))
                .setMaxResults(recordsPerPage)
                .getResultList();

    }

    /**
     * Zwraca listę wszystkich użytkowników w zadanej kolejności spełniających warunki zapytania
     *
     * @param page           numer strony do pobrania
     * @param recordsPerPage liczba rekordów na stronie
     * @param orderBy        nazwa kolumny, po której nastąpi sortowanie
     * @param order          kolejność sortowania
     * @param login          Login użytkownika
     * @param email          email
     * @param name           imie
     * @param surname        nazwisko
     * @param registered     czy użytkownik zarejestrowany
     * @param active         czy konto aktywne
     * @return lista wynikowa zapytania do bazy danych
     * @throws WrongParameterException zła nazwa kolumny
     */
    @PermitAll
    public List<Account> getAccountList(
            int page,
            int recordsPerPage,
            String orderBy,
            String order,
            String login,
            String email,
            String name,
            String surname,
            Boolean registered,
            Boolean active
    ) throws WrongParameterException {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Account> query = criteriaBuilder.createQuery(Account.class);
        Root<Account> table = query.from(Account.class);
        query.select(table);

        try {
            switch (order) {
                case "asc": {
                    query.orderBy(criteriaBuilder.asc(table.get(orderBy)));
                    break;

                }
                case "desc": {
                    query.orderBy(criteriaBuilder.desc(table.get(orderBy)));
                    break;

                }
            }
        } catch (IllegalArgumentException e) {
            throw ExceptionFactory.wrongParameterException();
        }

        if (login != null)
            query.where(criteriaBuilder.like(criteriaBuilder.lower(table.get("login")), addPercent(login.toLowerCase())));
        if (email != null)
            query.where(criteriaBuilder.like(criteriaBuilder.lower(table.get("email")), addPercent(email.toLowerCase())));
        if (name != null)
            query.where(criteriaBuilder.like(criteriaBuilder.lower(table.get("name")), addPercent(name.toLowerCase())));
        if (surname != null)
            query.where(criteriaBuilder.like(criteriaBuilder.lower(table.get("surname")), addPercent(surname.toLowerCase())));
        if (registered != null) query.where(criteriaBuilder.equal(table.get("registered"), registered));
        if (active != null) query.where(criteriaBuilder.equal(table.get("active"), active));


        return em
                .createQuery(query)
                .setFirstResult(recordsPerPage * (page - 1))
                .setMaxResults(recordsPerPage)
                .getResultList();
    }


    /**
     * Zwraca ilość rekordów po przefiltrowaniu
     *
     * @param login      Login użytkownika
     * @param email      email
     * @param name       imie
     * @param surname    nazwisko
     * @param registered czy użytkownik zarejestrowany
     * @param active     czy konto aktywne
     * @return ilość rekordów
     */
    @PermitAll
    public Long getAccountListSize(
            String login,
            String email,
            String name,
            String surname,
            Boolean registered,
            Boolean active
    ) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<Account> table = query.from(Account.class);
        query.select(criteriaBuilder.count(table));

        if (login != null)
            query.where(criteriaBuilder.like(criteriaBuilder.lower(table.get("login")), addPercent(login.toLowerCase())));
        if (email != null)
            query.where(criteriaBuilder.like(criteriaBuilder.lower(table.get("email")), addPercent(email.toLowerCase())));
        if (name != null)
            query.where(criteriaBuilder.like(criteriaBuilder.lower(table.get("name")), addPercent(name.toLowerCase())));
        if (surname != null)
            query.where(criteriaBuilder.like(criteriaBuilder.lower(table.get("surname")), addPercent(surname.toLowerCase())));
        if (registered != null) query.where(criteriaBuilder.equal(table.get("registered"), registered));
        if (active != null) query.where(criteriaBuilder.equal(table.get("active"), active));

        return em.createQuery(query).getSingleResult();
    }

    @Override
    @PermitAll
    public EntityManager getEm() {
        return em;
    }


    /**
     * Zwraca ilość rekordów po przefiltrowaniu
     *
     * @param name imie
     * @return ilość rekordów
     */
    @PermitAll
    public Long getAccountListSizeNameSurname(
            String name
    ) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<Account> table = query.from(Account.class);
        query.select(criteriaBuilder.count(table));

        query.where(
                criteriaBuilder.or(
                        criteriaBuilder.like(
                                criteriaBuilder.lower(table.get("name")), addPercent(name.toLowerCase())
                        ),
                        criteriaBuilder.like(
                                criteriaBuilder.lower(table.get("surname")), addPercent(name.toLowerCase())
                        )
                )
        );

        return em.createQuery(query).getSingleResult();
    }

    @PermitAll
    public List<Account> getWithLastLoginBefore(LocalDateTime dateTime) throws BaseApplicationException {
        TypedQuery<Account> query = getEm().createNamedQuery("account.findByLastLogInIsBefore", Account.class);
        try {
            query.setParameter("lastLogIn", dateTime);
            List<Account> res = query.getResultList();
            return res;
        } catch (NoResultException e) {
            throw ExceptionFactory.noAccountFound();
        } catch (OptimisticLockException ex) {
            throw ExceptionFactory.OptLockException();
        } catch (PersistenceException ex) {
            throw ExceptionFactory.databaseException();
        } catch (Exception ex) {
            throw ExceptionFactory.unexpectedFailException();
        }
    }

}
