package pl.lodz.p.it.ssbd2022.ssbd02.mow.endpoint;

import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.*;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.dto.*;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;

import java.util.List;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.*;

@Stateless
public class ReportEndpoint {
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
            throws NoAuthenticatedAccountFound, NoReviewFoundException {
        throw new UnsupportedOperationException();
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
