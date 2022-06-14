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

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import java.util.List;
import java.util.Optional;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.*;

@Stateless
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
        System.out.println("--------------------------------------------b");
        ReviewReport report = new ReviewReport();
        System.out.println("--------------------------------------------c");
        Account account = accountService.findByLogin(authenticationContext.getCurrentUsersLogin());
        System.out.println("--------------------------------------------d");
        Review review = reviewService.findById(createReviewReportDto.getReviewId());
        System.out.println("--------------------------------------------e");

        ReviewReportCause cause;
        System.out.println("--------------------------------------------f");
        List<ReviewReportCause> causes = reportService.getReviewReportCauses();
        System.out.println("Z jsona: " + createReviewReportDto.getCause());
        causes.forEach(e -> System.out.println("POWODU Z BAZY: "+e.getCause()));
        Optional<ReviewReportCause> optionalCause = causes.stream().filter(reviewReportCause -> reviewReportCause.getCause().equals(createReviewReportDto.getCause())).findFirst();
        if (optionalCause.isPresent()) {
            cause = optionalCause.get();
        } else {
            throw ExceptionFactory.noReviewReportCauseFoundException();
        }
        System.out.println("--------------------------------------------h");

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
}
