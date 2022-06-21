package pl.lodz.p.it.ssbd2022.ssbd02.mow.facade;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.AccountReport;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.ExceptionFactory;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.WrongParameterException;
import pl.lodz.p.it.ssbd2022.ssbd02.util.FacadeTemplate;
import pl.lodz.p.it.ssbd2022.ssbd02.util.LoggingInterceptor;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.listAllReports;

@Stateless
@Interceptors({LoggingInterceptor.class, MowFacadeAccessInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AccountFacade extends FacadeTemplate<Account> {

    @PersistenceContext(unitName = "ssbd02mowPU")
    private EntityManager em;

    public AccountFacade() {
        super(Account.class);
    }

    /**
     * Wyszukuje konto po loginie
     *
     * @param login login użytkownika do wyszukania
     * @return Konto użytkownika o podanym loginie
     * @throws BaseApplicationException gdy wystąpi problem z bazą danych
     */
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


    @Override
    protected EntityManager getEm() {
        return em;
    }


    /**
     * Pobiera listę zgłoszeń kont
     *
     * @param reviewed       czy sprawdzone
     * @param order          kierunek sortowania
     * @param page           numer strony
     * @param recordsPerPage liczba rekordów na stronie
     * @return lista zgłoszeń kont
     * @throws WrongParameterException niepoprawna kolejność sortowania
     */
    @RolesAllowed(listAllReports)
    public List<AccountReport> getAccountReportList(Boolean reviewed, String order, int page, int recordsPerPage) throws WrongParameterException {
        CriteriaBuilder criteriaBuilder = getEm().getCriteriaBuilder();
        CriteriaQuery<AccountReport> query = criteriaBuilder.createQuery(AccountReport.class);
        Root<AccountReport> table = query.from(AccountReport.class);
        query.select(table);

        switch (order) {
            case "asc": {
                query.orderBy(criteriaBuilder.asc(table.get("createdAt")));
                break;

            }
            case "desc": {
                query.orderBy(criteriaBuilder.desc(table.get("createdAt")));
                break;
            }
        }

        if (reviewed != null) query.where(criteriaBuilder.equal(table.get("reviewed"), reviewed));

        return getEm()
                .createQuery(query)
                .setFirstResult((page - 1) * recordsPerPage)
                .setMaxResults(recordsPerPage)
                .getResultList();
    }

    /**
     * Pobiera liczbę zgłoszeń kont
     *
     * @param reviewed czy sprawdzone
     * @return liczba zgłoszeń kont
     */
    @RolesAllowed(listAllReports)
    public Long getAccountReportListSize(Boolean reviewed) {
        CriteriaBuilder criteriaBuilder = getEm().getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<AccountReport> table = query.from(AccountReport.class);
        query.select(criteriaBuilder.count(table));
        if (reviewed != null) query.where(criteriaBuilder.equal(table.get("reviewed"), reviewed));
        return getEm().createQuery(query).getSingleResult();
    }
}
