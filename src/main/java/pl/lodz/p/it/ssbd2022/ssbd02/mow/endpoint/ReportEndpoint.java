package pl.lodz.p.it.ssbd2022.ssbd02.mow.endpoint;

import org.hibernate.service.spi.InjectService;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Review;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.ReviewReport;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.ReviewReportCause;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.*;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.dto.*;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.service.AccountService;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.service.ReportService;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.service.ReviewService;
import pl.lodz.p.it.ssbd2022.ssbd02.security.AuthenticationContext;
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

    @RolesAllowed(reportClient)
    public void reportAccount(CreateAccountReportDto createAccountReportDto)
            throws NoAuthenticatedAccountFound, NoAccountFound {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(reportPhotographer)
    public void reportPhotographer(CreatePhotographerReportDto createPhotographerReportDto)
            throws NoAuthenticatedAccountFound, NoPhotoFoundException {
        throw new UnsupportedOperationException();
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
}
