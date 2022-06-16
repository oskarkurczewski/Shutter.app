package pl.lodz.p.it.ssbd2022.ssbd02.mow.service;

import pl.lodz.p.it.ssbd2022.ssbd02.mow.facade.PhotographerReportFacade;
import pl.lodz.p.it.ssbd2022.ssbd02.util.LoggingInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.facade.ReviewReportFacade;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.*;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.*;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.facade.AccountReportCauseFacade;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.facade.AccountReportFacade;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.facade.MowAccessLevelFacade;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

import java.util.List;
import javax.inject.Inject;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.*;

/**
 * The type Report service.
 */
@Stateless
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class ReportService {

    @Inject
    ReviewReportFacade reviewReportFacade;

    @Inject
    private PhotographerReportFacade photographerReportFacade;

    @Inject
    private AccountReportCauseFacade accountReportCauseFacade;

    @Inject
    private AccountReportFacade accountReportFacade;

    @Inject
    private MowAccessLevelFacade accessLevelFacade;

    @Inject
    private AccountService accountService;

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

    @PermitAll
    public List<ReviewReportCause> getReviewReportCauses() throws BaseApplicationException {
        return reviewReportFacade.getReportCauses();
    }

    /**
     * Tworzy zgłoszenie na podanego klienta.
     *
     * @param report Obiekt zgłoszenia
     * @throws BaseApplicationException
     */
    @RolesAllowed(reportClient)
    public void addClientAccountReport(AccountReport report) throws BaseApplicationException {
            if (report.getReported().getLogin().equals(report.getReportee().getLogin())) {
                throw ExceptionFactory.selfReportException();
            }

            AccessLevelValue accessLevelValue = accountService.findAccessLevelValueByName("CLIENT");
            if (accessLevelFacade.getAccessLevelAssignmentForAccount(report.getReported(), accessLevelValue) == null) {
                throw ExceptionFactory.noAccountFound("exception.no_client_found");
            }

            accountReportFacade.persist(report);
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
    public void addReviewReport(ReviewReport report) throws BaseApplicationException {
        reviewReportFacade.persist(report);
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

    @PermitAll
    public AccountReportCause getAccountReportCause(String cause) throws DataNotFoundException {
        return  accountReportCauseFacade.getAccountReportCause(cause);
    }
}
