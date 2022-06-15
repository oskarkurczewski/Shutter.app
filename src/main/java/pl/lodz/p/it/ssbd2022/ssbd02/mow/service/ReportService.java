package pl.lodz.p.it.ssbd2022.ssbd02.mow.service;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.AccountReport;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.PhotographerReport;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.PhotographerReportCause;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.ReviewReport;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAccountReportFoundException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoPhotographerReportFoundException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoReviewReportFoundException;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.facade.PhotographerReportFacade;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.List;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.*;

/**
 * The type Report service.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class ReportService {

    @Inject
    private PhotographerReportFacade photographerReportFacade;

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
    public void addAccountReport(AccountReport report) {
        throw new UnsupportedOperationException();
    }

    /**
     * Dodaje nowe zgłoszenie fotografa.
     *
     * @param report obiekt zgłoszenia fotografa
     * @throws BaseApplicationException wystąpił nieznany błąd podczas dodawania do bazy danych
     */
    @RolesAllowed(reportPhotographer)
    public void addPhotographerReport(PhotographerReport report) throws BaseApplicationException {
        photographerReportFacade.persist(report);
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

    /**
     * Pobiera powód zgłoszenia fotografa na podstawie ciągu znaków
     *
     * @param cause ciąg znaków określający powód zgłoszenia
     * @return powód zgłoszenia fotografa
     */
    @RolesAllowed(reportPhotographer)
    public PhotographerReportCause getPhotographerReportCause(String cause) throws BaseApplicationException {
        return photographerReportFacade.getPhotographerReportCause(cause);
    }

    /**
     * Pobiera listę wszystkich zgłoszeń fotografów
     *
     * @return lista zgłoszeń fotografów
     */
    @RolesAllowed(reportPhotographer)
    public List<String> getAllPhotographerReportCauses() {
        return photographerReportFacade.getAllPhotographerReportCauses();
    }

    /**
     * Pobiera listę zgłoszeń danego fotografa przez danego użytkownika
     *
     * @param photographerLogin nazwa użytkownika fotografa
     * @param reporterLogin     nazwa użytkownika zgłaszającego
     * @return lista zgłoszeń fotografa
     */
    @RolesAllowed(reportPhotographer)
    public List<PhotographerReport> getPhotographerReportByPhotographerLoginAndReporterLogin(
            String photographerLogin,
            String reporterLogin
    ) {
        return photographerReportFacade.getPhotographerReportByPhotographerLoginAndReporterLogin(
                photographerLogin,
                reporterLogin
        );
    }
}
