package pl.lodz.p.it.ssbd2022.ssbd02.mow.service;

import org.hibernate.exception.ConstraintViolationException;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.AccountReport;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.AccountReportCause;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.PhotographerReport;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.ReviewReport;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.*;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.facade.AccountReportCauseFacade;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.facade.AccountReportFacade;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.PersistenceException;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.*;
import static pl.lodz.p.it.ssbd2022.ssbd02.util.ConstraintNames.IDENTICAL_EMAIL;
import static pl.lodz.p.it.ssbd2022.ssbd02.util.ConstraintNames.IDENTICAL_LOGIN;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class ReportService {

    @Inject
    private AccountReportCauseFacade accountReportCauseFacade;

    @Inject
    private AccountReportFacade accountReportFacade;

    @PermitAll
    public AccountReport findAccountReportById(Long id) throws NoAccountReportFoundException {
        throw new UnsupportedOperationException();
    }

    @PermitAll
    public PhotographerReport findPhotographerReportById(Long id) throws NoPhotographerReportFoundException {
        throw new UnsupportedOperationException();
    }

    @PermitAll
    public ReviewReport findReviewReportById(Long id) throws NoReviewReportFoundException {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(reportClient)
    public void addAccountReport(AccountReport report) throws BaseApplicationException {
            accountReportFacade.persist(report);
    }

    @RolesAllowed(reportPhotographer)
    public void addPhotographerReport(PhotographerReport report) {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(reportReview)
    public void addReviewReport(ReviewReport report) {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(listAllReports)
    public void listAllAccountReports() {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(listAllReports)
    public void listAllPhotographerReports() {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(listAllReports)
    public void listAllReviewReports() {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(resolveReport)
    public void resolveReviewReport(ReviewReport report) {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(resolveReport)
    public void resolvePhotographerReport(PhotographerReport report) {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(resolveReport)
    public void resolveAccountReport(AccountReport report) {
        throw new UnsupportedOperationException();
    }

    @PermitAll
    public AccountReportCause getAccountReportCause(String cause) throws DataNotFoundException {
        return  accountReportCauseFacade.getAccountReportCause(cause);
    }
}
