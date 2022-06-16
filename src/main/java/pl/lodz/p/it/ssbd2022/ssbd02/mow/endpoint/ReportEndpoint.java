package pl.lodz.p.it.ssbd2022.ssbd02.mow.endpoint;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Review;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.ReviewReport;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.ReviewReportCause;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.PhotographerReport;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.*;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.dto.*;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.service.AccountService;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.service.ReportService;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.service.ReviewService;
import pl.lodz.p.it.ssbd2022.ssbd02.security.AuthenticationContext;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.service.ProfileService;
import pl.lodz.p.it.ssbd2022.ssbd02.util.AbstractEndpoint;
import pl.lodz.p.it.ssbd2022.ssbd02.util.LoggingInterceptor;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

import java.util.List;
import java.util.Optional;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.*;

@Stateless
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class ReportEndpoint extends AbstractEndpoint {
    @Inject
    ReviewService reviewService;

    @Inject
    ReportService reportService;

    @Inject
    AccountService accountService;

    @Inject
    private AuthenticationContext authenticationContext;

    @Inject
    private ProfileService profileService;

    @RolesAllowed(reportClient)
    public void reportAccount(CreateAccountReportDto createAccountReportDto)
            throws NoAuthenticatedAccountFound, NoAccountFound {
        throw new UnsupportedOperationException();
    }

    /**
     * Dodaje nowe zgłoszenie fotografa.
     *
     * @param createPhotographerReportDto obiekt DTO zgłoszenia fotografa
     * @throws WrongParameterException  podano nieprawidłowy powód zgłoszenia
     * @throws CannotChangeException    dany użytkownik zgłosił już danego fotografa
     * @throws BaseApplicationException wystąpił nieznany błąd podczas dodawania do bazy danych
     */
    @RolesAllowed(reportPhotographer)
    public void reportPhotographer(CreatePhotographerReportDto createPhotographerReportDto)
            throws BaseApplicationException {
        String cause = createPhotographerReportDto.getCause();
        if (!reportService.getAllPhotographerReportCauses().contains(cause)) {
            throw ExceptionFactory.wrongCauseNameException();
        }

        List<PhotographerReport> reportOld = reportService.getPhotographerReportByPhotographerLoginAndReporterLogin(
                createPhotographerReportDto.getPhotographerLogin(),
                authenticationContext.getCurrentUsersLogin()
        );

        if (!reportOld.isEmpty()) {
            throw ExceptionFactory.photographerAlreadyReportedException();
        }

        PhotographerReport report = new PhotographerReport();
        report.setPhotographer(profileService.findPhotographerInfo(createPhotographerReportDto.getPhotographerLogin()));
        report.setAccount(accountService.findByLogin(authenticationContext.getCurrentUsersLogin()));
        report.setCause(reportService.getPhotographerReportCause(createPhotographerReportDto.getCause()));
        report.setReviewed(false);
        reportService.addPhotographerReport(report);
    }

    @RolesAllowed(reportReview)
    public void reportReview(CreateReviewReportDto createReviewReportDto)
            throws BaseApplicationException {
        ReviewReport report = new ReviewReport();
        Account account = accountService.findByLogin(authenticationContext.getCurrentUsersLogin());
        Review review = reviewService.findById(createReviewReportDto.getReviewId());

        ReviewReportCause cause;
        List<ReviewReportCause> causes = reportService.getReviewReportCauses();
        Optional<ReviewReportCause> optionalCause = causes.stream().filter(reviewReportCause -> reviewReportCause.getCause().equals(createReviewReportDto.getCause())).findFirst();
        if (optionalCause.isPresent()) {
            cause = optionalCause.get();
        } else {
            throw ExceptionFactory.noReviewReportCauseFoundException();
        }

        report.setAccount(account);
        report.setReview(review);
        report.setCause(cause);
        report.setReviewed(false);
        reportService.addReviewReport(report);
    }

    @RolesAllowed(listAllReports)
    public List<AccountReportDto> listAllAccountReports() {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(listAllReports)
    public List<PhotographerReportDto> listAllPhotographerReports() {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(listAllReports)
    public List<ReviewReportDto> listAllReviewReports() {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(resolveReport)
    public void resolveAccountReport(Long reportId) {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(resolveReport)
    public void resolvePhotographerReport(Long photographerId) {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(resolveReport)
    public void resolveReviewReport(Long reviewId) {
        throw new UnsupportedOperationException();
    }

    /**
     * Pobiera listę wszystkich zgłoszeń fotografów
     *
     * @return lista zgłoszeń fotografów
     */
    @RolesAllowed(reportPhotographer)
    public List<String> getAllPhotographerReportCauses() {
        return reportService.getAllPhotographerReportCauses();
    }
}
