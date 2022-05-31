package pl.lodz.p.it.ssbd2022.ssbd02.mok.facade;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.AccountChangeLog;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.ExceptionFactory;
import pl.lodz.p.it.ssbd2022.ssbd02.util.FacadeTemplate;
import pl.lodz.p.it.ssbd2022.ssbd02.util.LoggingInterceptor;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Stateless
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AccountChangeLogFacade extends FacadeTemplate<AccountChangeLog> {
    @PersistenceContext(unitName = "ssbd02mokPU")
    private EntityManager em;

    public AccountChangeLogFacade() {
        super(AccountChangeLog.class);
    }

    @Override
    protected EntityManager getEm() {
        return em;
    }


    /**
     * Zwraca historię zmian dla konta
     *
     * @param login          Login użytkownika, którego historia zmian konta ma być wyszukana
     * @param page           numer strony
     * @param recordsPerPage liczba rekordów na stronę
     * @param orderBy        kolumna po której następuje sortowanie
     * @param orderAsc       kolejność sortowania
     * @return Historia zmian konta
     * @throws BaseApplicationException jeżeli użytkownik o podanym loginie nie istnieje
     */
    @PermitAll
    public List<AccountChangeLog> findByLogin(
            String login,
            String orderBy,
            Boolean orderAsc,
            int recordsPerPage,
            int page
    ) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<AccountChangeLog> query = criteriaBuilder.createQuery(AccountChangeLog.class);
        Root<AccountChangeLog> table = query.from(AccountChangeLog.class);
        query.select(table);

        query.where(criteriaBuilder.equal(table.get("changedBy").get("login"), login));

        if (orderAsc) {
            query.orderBy(criteriaBuilder.asc(table.get(orderBy)));
        } else {
            query.orderBy(criteriaBuilder.desc(table.get(orderBy)));
        }

        return em
                .createQuery(query)
                .setFirstResult(recordsPerPage * (page - 1))
                .setMaxResults(recordsPerPage)
                .getResultList();

    }

    @PermitAll
    public Long getListSize(String login) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<AccountChangeLog> table = query.from(AccountChangeLog.class);
        query.select(criteriaBuilder.count(table));
        query.where(criteriaBuilder.equal(table.get("changedBy").get("login"), login));

        return em.createQuery(query).getSingleResult();
    }

    @Override
    @PermitAll
    public AccountChangeLog persist(AccountChangeLog entity) throws BaseApplicationException {
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
}
