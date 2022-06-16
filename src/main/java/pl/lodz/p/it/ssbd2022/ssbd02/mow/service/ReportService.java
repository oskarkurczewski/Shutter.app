package pl.lodz.p.it.ssbd2022.ssbd02.mow.service;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.AccountReport;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.PhotographerReport;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.ReviewReport;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.ReviewReportCause;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAccountReportFoundException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoPhotographerReportFoundException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoReviewReportFoundException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.WrongParameterException;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.dto.*;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.facade.AccountFacade;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.facade.PhotographerReportFacade;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.facade.ReviewReportFacade;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

import java.util.List;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.*;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class ReportService {
    @Inject
    ReviewReportFacade reviewReportFacade;

    @Inject
    AccountFacade accountFacade;

    @Inject
    PhotographerReportFacade photographerReportFacade;

    @Inject
    ReviewReportFacade reviewReportFacade;

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

    @RolesAllowed(reportClient)
    public void addAccountReport(AccountReport report) {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(reportPhotographer)
    public void addPhotographerReport(PhotographerReport report) {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(reportReview)
    public void addReviewReport(ReviewReport report) throws BaseApplicationException {
        reviewReportFacade.persist(report);
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
        List<AccountReport> list = accountFacade.getAccountReportList(req.getReviewed(), req.getOrder(), req.getPage(), req.getRecordsPerPage());
        Long allRecords = accountFacade.getAccountReportListSize(req.getReviewed());
        return new ListResponseDto<>(
                req.getPage(),
                (int) Math.ceil((double) allRecords / req.getRecordsPerPage()),
                req.getRecordsPerPage(),
                allRecords,
                list.stream().map(AccountReportDto::new).collect(Collectors.toList())
        );
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
        List<PhotographerReport> list = photographerReportFacade.getPhotographerReportList(req.getReviewed(), req.getOrder(), req.getPage(), req.getRecordsPerPage());
        Long allRecords = photographerReportFacade.getPhotographerReportListSize(req.getReviewed());
        return new ListResponseDto<>(
                req.getPage(),
                (int) Math.ceil((double) allRecords / req.getRecordsPerPage()),
                req.getRecordsPerPage(),
                allRecords,
                list.stream().map(PhotographerReportDto::new).collect(Collectors.toList())
        );
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
        List<ReviewReport> list = reviewReportFacade.getReviewReportList(req.getReviewed(), req.getOrder(), req.getPage(), req.getRecordsPerPage());
        Long allRecords = reviewReportFacade.getReviewReportListSize(req.getReviewed());
        return new ListResponseDto<>(
                req.getPage(),
                (int) Math.ceil((double) allRecords / req.getRecordsPerPage()),
                req.getRecordsPerPage(),
                allRecords,
                list.stream().map(ReviewReportDto::new).collect(Collectors.toList())
        );
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
}
