package pl.lodz.p.it.ssbd2022.ssbd02.mow.facade;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.*;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.DatabaseException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.ExceptionFactory;
import pl.lodz.p.it.ssbd2022.ssbd02.util.FacadeTemplate;
import pl.lodz.p.it.ssbd2022.ssbd02.util.LoggingInterceptor;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.*;

import java.util.List;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.reportReview;

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

    @Override
    @RolesAllowed(reportReview)
    public ReviewReport persist(ReviewReport entity) throws BaseApplicationException {
        try {
            return super.persist(entity);
        } catch (OptimisticLockException ex) {
            throw ExceptionFactory.OptLockException();
        } catch (PersistenceException ex) {
            throw ExceptionFactory.databaseException();
        } catch (Exception ex) {
            System.out.println(ex);
            throw ExceptionFactory.unexpectedFailException();
        }
    }

    @PermitAll
    public List<ReviewReportCause> getReportCauses() throws BaseApplicationException {
        TypedQuery<ReviewReportCause> query = getEm().createNamedQuery(
                "review_report_cause.getAll",
                ReviewReportCause.class);
        try {
            return query.getResultList();
        } catch (OptimisticLockException ex) {
            throw ExceptionFactory.OptLockException();
        } catch (PersistenceException ex) {
            throw ExceptionFactory.databaseException();
        } catch (Exception ex) {
            throw ExceptionFactory.unexpectedFailException();
        }
    }
}
