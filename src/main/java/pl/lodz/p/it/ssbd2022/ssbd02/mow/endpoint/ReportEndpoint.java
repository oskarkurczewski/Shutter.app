package pl.lodz.p.it.ssbd2022.ssbd02.mow.endpoint;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Review;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.ReviewReport;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.ReviewReportCause;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.PhotographerReport;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.AccountReport;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.AccountReportCause;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.*;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.dto.*;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.service.AccountService;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.service.ReportService;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.service.ReviewService;
import pl.lodz.p.it.ssbd2022.ssbd02.security.AuthenticationContext;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.service.ProfileService;
import pl.lodz.p.it.ssbd2022.ssbd02.util.AbstractEndpoint;
import pl.lodz.p.it.ssbd2022.ssbd02.util.LoggingInterceptor;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.*;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors({LoggingInterceptor.class})
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


    /**
     * Tworzy zgłoszenie na podanego klienta.
     *
     * @param createAccountReportDto Obiekt przedstawiający login zgłoszonego klienta oraz powód zgłoszenia.
     * @throws BaseApplicationException W przypadku niepowodzenia operacji.
     */
    @RolesAllowed(reportClient)
    public void reportClientAccount(CreateAccountReportDto createAccountReportDto)
            throws BaseApplicationException {
        Account reported = accountService.findByLogin(createAccountReportDto.getReportedLogin());
        AccountReportCause reportCause = reportService.getAccountReportCause(createAccountReportDto.getCause());
        Account reportee = accountService.findByLogin(authenticationContext.getCurrentUsersLogin());

        AccountReport accountReport = new AccountReport();
        accountReport.setReported(reported);
        accountReport.setCause(reportCause);
        accountReport.setReportee(reportee);
        accountReport.setReviewed(false);

        reportService.addClientAccountReport(accountReport);
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
        String currentUserLogin = authenticationContext.getCurrentUsersLogin();
        if (createPhotographerReportDto.getPhotographerLogin().equals(currentUserLogin)) {
            throw ExceptionFactory.cannotPerformOnSelfException();
        }

        String cause = createPhotographerReportDto.getCause();
        if (!reportService.getAllPhotographerReportCauses().contains(cause)) {
            throw ExceptionFactory.wrongCauseNameException();
        }

        List<PhotographerReport> reportOld = reportService.getPhotographerReportByPhotographerLoginAndReporterLogin(
                createPhotographerReportDto.getPhotographerLogin(),
                currentUserLogin
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

    /**
     * Zwraca wszystkie powody zgłoszeń fotografów
     *
     * @return powody zgłoszeń fotografów
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @PermitAll
    public List<String> getAllReviewReportCauses() throws BaseApplicationException {
        return reportService.getReviewReportCauses().stream().map(ReviewReportCause::getCause).collect(Collectors.toList());
    }

    /**
     * Zgłasza recenzję fotografa używając podanych informacji
     *
     * @param createReviewReportDto informację potrzebne do dokonania zgłoszenia
     * @throws BaseApplicationException niepowodzenie operacji
     */
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


    /**
     * Pobiera listę zgłoszeń kont
     *
     * @param req obiekt dto zapytania
     * @return lista zgłoszeń kont
     * @throws WrongParameterException niepoprawna kolejność sortowania
     */
    @RolesAllowed(listAllReports)
    public ListResponseDto<AccountReportDto> getAccountReportList(GetReportRequestDto req) throws WrongParameterException {
        return reportService.getAccountReportList(req);
    }

    /**
     * Pobiera listę zgłoszeń fotografów
     *
     * @param req obiekt dto zapytania
     * @return lista zgłoszeń fotografów
     * @throws WrongParameterException niepoprawna kolejność sortowania
     */
    @RolesAllowed(listAllReports)
    public ListResponseDto<PhotographerReportDto> getPhotographerReportList(GetReportRequestDto req) throws WrongParameterException {
        return reportService.getPhotographerReportList(req);
    }

    /**
     * Pobiera listę zgłoszeń recenzji
     *
     * @param req obiekt dto zapytania
     * @return lista zgłoszeń recenzji
     * @throws WrongParameterException niepoprawna kolejność sortowania
     */
    @RolesAllowed(listAllReports)
    public ListResponseDto<ReviewReportDto> getReviewReportList(GetReportRequestDto req) throws WrongParameterException {
        return reportService.getReviewReportList(req);
    }


    /**
     * Rozpatruje zgłoszenie użytkownika
     *
     * @param reportId identyfikator zgłoszenia
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @RolesAllowed(resolveReport)
    public void resolveAccountReport(Long reportId) throws BaseApplicationException {
        reportService.resolveAccountReport(reportService.findAccountReportById(reportId));
    }

    /**
     * Rozpatruje zgłoszenie fotografa
     *
     * @param photographerId identyfikator zgłoszenia
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @RolesAllowed(resolveReport)
    public void resolvePhotographerReport(Long photographerId) throws BaseApplicationException {
        reportService.resolvePhotographerReport(reportService.findPhotographerReportById(photographerId));
    }

    /**
     * Rozpatruje zgłoszenie recenzji
     *
     * @param reviewId identyfikator recenzji
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @RolesAllowed(resolveReport)
    public void resolveReviewReport(Long reviewId) throws BaseApplicationException {
        reportService.resolveReviewReport(reportService.findReviewReportById(reviewId));
    }

    /**
     * Pobiera listę wszystkich zgłoszeń fotografów
     *
     * @return lista zgłoszeń fotografów
     */
    @PermitAll
    public List<String> getAllPhotographerReportCauses() {
        return reportService.getAllPhotographerReportCauses();
    }
}
