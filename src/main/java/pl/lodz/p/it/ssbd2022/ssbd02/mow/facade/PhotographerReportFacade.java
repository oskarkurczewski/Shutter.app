package pl.lodz.p.it.ssbd2022.ssbd02.mow.facade;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.PhotographerReport;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.PhotographerReportCause;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.util.FacadeTemplate;
import pl.lodz.p.it.ssbd2022.ssbd02.util.LoggingInterceptor;

import javax.annotation.security.PermitAll;
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

@Stateless
@Interceptors({LoggingInterceptor.class, MowFacadeAccessInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class PhotographerReportFacade extends FacadeTemplate<PhotographerReport> {
    @PersistenceContext(unitName = "ssbd02mowPU")
    private EntityManager em;

    public PhotographerReportFacade() {
        super(PhotographerReport.class);
    }

    @Override
    @PermitAll
    public EntityManager getEm() {
        return em;
    }

    @Override
    public PhotographerReport persist(PhotographerReport entity) throws BaseApplicationException {
        return super.persist(entity);
    }

    /**
     * Pobiera listę wszystkich zgłoszeń fotografów
     *
     * @return lista zgłoszeń fotografów
     */
    public List<PhotographerReportCause> getAllPhotographerReportCauses() {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<PhotographerReportCause> query = criteriaBuilder.createQuery(PhotographerReportCause.class);
        Root<PhotographerReportCause> root = query.from(PhotographerReportCause.class);
        query.select(root);
        return em.createQuery(query).getResultList();
    }

    /**
     * Pobiera powód zgłoszenia fotografa na podstawie ciągu znaków
     *
     * @param cause ciąg znaków określający powód zgłoszenia
     * @return powód zgłoszenia fotografa
     */
    public PhotographerReportCause getPhotographerReportCause(String cause) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<PhotographerReportCause> query = criteriaBuilder.createQuery(PhotographerReportCause.class);
        Root<PhotographerReportCause> root = query.from(PhotographerReportCause.class);
        query.select(root);
        query.where(criteriaBuilder.equal(root.get("cause"), cause));
        return em.createQuery(query).getSingleResult();
    }

    /**
     * Pobiera listę zgłoszeń danego fotografa przez danego użytkownika
     *
     * @param photographerLogin nazwa użytkownika fotografa
     * @param reporterLogin     nazwa użytkownika zgłaszającego
     * @return lista zgłoszeń fotografa
     */
    public List<PhotographerReport> getPhotographerReportByPhotographerLongAndReporterId(String photographerLogin, String reporterLogin) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<PhotographerReport> query = criteriaBuilder.createQuery(PhotographerReport.class);
        Root<PhotographerReport> root = query.from(PhotographerReport.class);
        query.select(root);
        query.where(
                criteriaBuilder.equal(root.get("photographer").get("account").get("login"), photographerLogin),
                criteriaBuilder.equal(root.get("account").get("login"), reporterLogin)
        );
        return em.createQuery(query).getResultList();

    }

}
