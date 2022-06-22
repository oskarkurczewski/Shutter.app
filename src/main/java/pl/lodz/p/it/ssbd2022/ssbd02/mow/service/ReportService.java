package pl.lodz.p.it.ssbd2022.ssbd02.mow.service;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.*;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.DataNotFoundException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.ExceptionFactory;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.WrongParameterException;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.dto.*;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.facade.*;
import pl.lodz.p.it.ssbd2022.ssbd02.util.LoggingInterceptor;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.util.List;
import java.util.stream.Collectors;

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

    @Inject
    private AccountFacade accountFacade;

    /**
     * Pozyskuje zgłoszenie konta o podanym identyfikatorze
     *
     * @param id identyfikator zgłoszenia
     * @return zgłoszenie konta o podanym identyfikatorze
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @PermitAll
    public AccountReport findAccountReportById(Long id) throws BaseApplicationException {
        return accountReportFacade.find(id);
    }

    /**
     * Pozyskuje zgłoszenie fotografa po podanym identyfikatorze
     *
     * @param id identyfikator zgłoszenia
     * @return zgłoszenie fotografa o podanym identyfikatorze
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @PermitAll
    public PhotographerReport findPhotographerReportById(Long id) throws BaseApplicationException {
        return photographerReportFacade.find(id);
    }

    /**
     * Pozyskuje zgłoszenie recenzji po podanym identyfikatorze
     *
     * @param id identyfikator zgłoszenia
     * @return zgłoszenie recenzji o podanym identyfikatorze
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @PermitAll
    public ReviewReport findReviewReportById(Long id) throws BaseApplicationException {
        return reviewReportFacade.find(id);
    }

    /**
     * Pozyskuje list powodów zgłoszeń recenzji
     *
     * @return lista powodów zgłoszeń recenzji
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @PermitAll
    public List<ReviewReportCause> getReviewReportCauses() throws BaseApplicationException {
        return reviewReportFacade.getReportCauses();
    }

    /**
     * Tworzy zgłoszenie na podanego klienta.
     *
     * @param report Obiekt zgłoszenia
     * @throws BaseApplicationException niepowodzenie operacji
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


    /**
     * Rozpatruje zgłoszenie recenzji
     *
     * @param report zgłoszenie, które ma zostać rozpatrzona
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @RolesAllowed(resolveReport)
    public void resolveReviewReport(ReviewReport report) throws BaseApplicationException {
        report.setReviewed(true);
        reviewReportFacade.update(report);
    }

    /**
     * Rozpatruje zgłoszenie fotografa
     *
     * @param report zgłoszenie, które ma zostać rozpatrzona
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @RolesAllowed(resolveReport)
    public void resolvePhotographerReport(PhotographerReport report) throws BaseApplicationException {
        report.setReviewed(true);
        photographerReportFacade.update(report);
    }

    /**
     * Rozpatruje zgłoszenie użytkownika
     *
     * @param report zgłoszenie, które ma zostać rozpatrzona
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @RolesAllowed(resolveReport)
    public void resolveAccountReport(AccountReport report) throws BaseApplicationException {
        report.setReviewed(true);
        accountReportFacade.update(report);
    }

    /**
     * Pobiera powód zgłoszenia fotografa na podstawie ciągu znaków
     *
     * @param cause ciąg znaków określający powód zgłoszenia
     * @return powód zgłoszenia fotografa
     * @throws BaseApplicationException niepowodzenie operacji
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
    @PermitAll
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

    /**
     * Pobiera przyczynę zgłoszenia użytkownika na podstawie jej nazwy
     *
     * @param cause nazwa przyczyny zgłoszenia
     * @return przyczyna zgłoszenia
     * @throws DataNotFoundException niepowodzenie operacji
     */
    @PermitAll
    public AccountReportCause getAccountReportCause(String cause) throws DataNotFoundException {
        return accountReportCauseFacade.getAccountReportCause(cause);
    }
}
