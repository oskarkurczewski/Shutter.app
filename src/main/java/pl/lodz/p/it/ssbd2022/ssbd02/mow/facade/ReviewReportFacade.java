package pl.lodz.p.it.ssbd2022.ssbd02.mow.facade;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.ReviewReport;
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
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.listAllReports;

@Stateless
@Interceptors({LoggingInterceptor.class, MowFacadeAccessInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class ReviewReportFacade extends FacadeTemplate<ReviewReport> {
    @PersistenceContext(unitName = "ssbd02mowPU")
    private EntityManager em;

    public ReviewReportFacade() {
        super(ReviewReport.class);
    }

    @Override
    @PermitAll
    public EntityManager getEm() {
        return em;
    }


    /**
     * Pobiera listę zgłoszeń recenzji
     *
     * @param reviewed       czy sprawdzone
     * @param order          kierunek sortowania
     * @param page           numer strony
     * @param recordsPerPage liczba rekordów na stronie
     * @return lista zgłoszeń recenzji
     * @throws WrongParameterException niepoprawna kolejność sortowania
     */
    @RolesAllowed(listAllReports)
    public List<ReviewReport> getReviewReportList(Boolean reviewed, String order, int page, int recordsPerPage) throws WrongParameterException {
        CriteriaBuilder criteriaBuilder = getEm().getCriteriaBuilder();
        CriteriaQuery<ReviewReport> query = criteriaBuilder.createQuery(ReviewReport.class);
        Root<ReviewReport> table = query.from(ReviewReport.class);
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
     * Pobiera liczbę zgłoszeń recenzji
     *
     * @param reviewed czy sprawdzone
     * @return liczba zgłoszeń recenzji
     */
    @RolesAllowed(listAllReports)
    public Long getReviewReportListSize(Boolean reviewed) {
        CriteriaBuilder criteriaBuilder = getEm().getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        Root<ReviewReport> table = query.from(ReviewReport.class);
        query.select(criteriaBuilder.count(table));
        if (reviewed != null) query.where(criteriaBuilder.equal(table.get("reviewed"), reviewed));
        return getEm().createQuery(query).getSingleResult();
    }
}
