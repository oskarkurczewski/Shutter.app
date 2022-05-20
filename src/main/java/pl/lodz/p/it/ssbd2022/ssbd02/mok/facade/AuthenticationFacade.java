package pl.lodz.p.it.ssbd2022.ssbd02.mok.facade;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.AccessLevelAssignment;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.AccessLevelValue;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.PhotographerInfo;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.*;
import pl.lodz.p.it.ssbd2022.ssbd02.util.FacadeTemplate;
import pl.lodz.p.it.ssbd2022.ssbd02.util.LoggingInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd02.util.FacadeAccessInterceptor;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static pl.lodz.p.it.ssbd2022.ssbd02.util.ConstraintNames.IDENTICAL_EMAIL;
import static pl.lodz.p.it.ssbd2022.ssbd02.util.ConstraintNames.IDENTICAL_LOGIN;

@Stateless
@Interceptors({LoggingInterceptor.class, FacadeAccessInterceptor.class})
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
     * Zwraca listę wszystkich użytkowników w zadanej kolejności spełniających warunki zapytania
     *
     * @param page           numer strony do pobrania
     * @param recordsPerPage liczba rekordów na stronie
     * @param orderBy        nazwa kolumny, po której nastąpi sortowanie
     * @param order          kolejność sortowania
     * @param login          nazwa użytkownika
     * @param email          email
     * @param name           imie
     * @param surname        nazwisko
     * @param registered     czy użytkownik zarejestrowany
     * @param active         czy konto aktywne
     * @return lista wynikowa zapytania do bazy danych
     * @throws WrongParameterException w przypadku gdy podano złą nazwę kolumny lub kolejność sortowania
     */
    public List<String> getAccountList(
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
                default: {
                    throw ExceptionFactory.wrongParameterException();
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
     * dodaje znak '%' na początku i na końcu struny
     *
     * @param s struna
     * @return struna wynikowa
     */
    private String addPercent(String s) {
        return "%" + s + "%";
    }

    /**
     * Zwraca ilość rekordów po przefiltrowaniu
     *
     * @param login          nazwa użytkownika
     * @param email          email
     * @param name           imie
     * @param surname        nazwisko
     * @param registered     czy użytkownik zarejestrowany
     * @param active         czy konto aktywne
     * @return ilość rekordów
     */
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


}
