package pl.lodz.p.it.ssbd2022.ssbd02.controllers;

import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.*;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.dto.*;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.endpoint.ReportEndpoint;
import pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint.Order;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/report")
public class ReportController extends AbstractController {

    @Inject
    private ReportEndpoint reportEndpoint;

    /**
     * Punkt końcowy tworzący zgłoszenie klienta o podanych danych
     *
     * @param createAccountReportDto Obiekt przedstawiający dane zawierające login zgłoszonego klienta oraz powód.
     * @return odpowiedź HTTP
     * @throws BaseApplicationException W przypadku niepowodzenia operacji
     */
    @POST
    @Path("/account")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response reportClientAccount(@NotNull @Valid CreateAccountReportDto createAccountReportDto)
            throws BaseApplicationException {
        repeat(() -> reportEndpoint.reportClientAccount(createAccountReportDto), reportEndpoint);
        return Response.status(Response.Status.OK).build();
    }

    /**
     * Punkt końcowy tworzący zgłoszenie fotografa o podanych danych
     *
     * @param createPhotographerReportDto obiekt DTO zawierający dane zgłoszenia
     * @return odpowiedź HTTP
     * @throws WrongParameterException  podano nieprawidłowy powód zgłoszenia
     * @throws CannotChangeException    dany użytkownik zgłosił już danego fotografa
     * @throws BaseApplicationException wystąpił nieznany błąd podczas dodawania do bazy danych
     */
    @POST
    @Path("/photographer")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response reportPhotographer(@NotNull @Valid CreatePhotographerReportDto createPhotographerReportDto)
            throws BaseApplicationException {
        repeat(() -> reportEndpoint.reportPhotographer(createPhotographerReportDto), reportEndpoint);
        return Response.ok().build();
    }

    /**
     * Punkt końcowy zwracający listę powodów zgłoszeń fotografa
     *
     * @return wszystkie istniejące w aplikacji powody zgłoszeń fotografów
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @GET
    @Path("/photographer/report-causes")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getAllPhotographerReportCauses() throws BaseApplicationException {
        return repeat(() -> reportEndpoint.getAllPhotographerReportCauses(), reportEndpoint);
    }


    /**
     * Punkt końcowy tworzący nowe zgłoszenie recenzji o podanych danych
     *
     * @param createReviewReportDto dane zgłoszenia recenzji
     * @return odpowiedź HTTP
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @POST
    @Path("/review")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response reportReview(@NotNull @Valid CreateReviewReportDto createReviewReportDto)
            throws BaseApplicationException {
        repeat(() -> reportEndpoint.reportReview(createReviewReportDto), reportEndpoint);
        return Response.status(Response.Status.CREATED).build();
    }

    /**
     * Punkt końcowy pobierający wszystkie występujące w aplikacji powody zgłoszeń recenzji
     *
     * @return lista powodów zgłoszeń recenzji
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @GET
    @Path("/review/report-causes")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getAllReviewReportCauses() throws BaseApplicationException {
        return repeat(() -> reportEndpoint.getAllReviewReportCauses(), reportEndpoint);
    }


    /**
     * Punkt końcowy zwracający listę zgłoszeń kont
     *
     * @param pageNo numer strony
     * @param order kolejność sortowania
     * @param recordsPerPage ilość krotek na stronę
     * @param reviewed czy zgłoszenie zostało rozpatrzone
     * @return lista zgłoszeń kont
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @GET
    @Path("/list/account")
    @Produces(MediaType.APPLICATION_JSON)
    public ListResponseDto<AccountReportDto> listAllAccountReports(
            @QueryParam("pageNo") @DefaultValue("1") int pageNo,
            @QueryParam("recordsPerPage") @NotNull int recordsPerPage,
            @QueryParam("order") @Order @DefaultValue("asc") String order,
            @QueryParam("reviewed") Boolean reviewed
    ) throws BaseApplicationException {
        return repeat(() -> reportEndpoint.getAccountReportList(
                        new GetReportRequestDto(reviewed, order, pageNo, recordsPerPage)
                ), reportEndpoint
        );
    }

    /**
     * Punkt końcowy zwracający zgłsozenia fotografów
     *
     * @param pageNo numer strony
     * @param recordsPerPage ilość krotek na stronę
     * @param reviewed czy zgłoszenie zostało rozpatrzone
     * @param order kolejność sortowania
     * @return lista zgłoszeń fotografów
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @GET
    @Path("/list/photographer")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public ListResponseDto<PhotographerReportDto> listAllPhotographerReports(
            @QueryParam("pageNo") @DefaultValue("1") int pageNo,
            @QueryParam("recordsPerPage") @NotNull int recordsPerPage,
            @QueryParam("order") @Order @DefaultValue("asc") String order,
            @QueryParam("reviewed") Boolean reviewed
    ) throws BaseApplicationException {
        return repeat(() -> reportEndpoint.getPhotographerReportList(
                        new GetReportRequestDto(reviewed, order, pageNo, recordsPerPage)
                ), reportEndpoint
        );
    }

    /**
     * Punkt końcowy zwracający listę zgłoszeń recenzji
     *
     * @param pageNo numer strony
     * @param order kolejność sortowania
     * @param reviewed czy zgłoszenie zostało rozpatrzone
     * @param recordsPerPage ilość krotek na stronę
     * @return lista zgłoszeń recenzji
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @GET
    @Path("/list/review")
    @Produces(MediaType.APPLICATION_JSON)
    public ListResponseDto<ReviewReportDto> listAllReviewReports(
            @QueryParam("pageNo") @DefaultValue("1") int pageNo,
            @QueryParam("recordsPerPage") @NotNull int recordsPerPage,
            @QueryParam("order") @Order @DefaultValue("asc") String order,
            @QueryParam("reviewed") Boolean reviewed
    ) throws BaseApplicationException {
        return repeat(() -> reportEndpoint.getReviewReportList(
                        new GetReportRequestDto(reviewed, order, pageNo, recordsPerPage)
                ), reportEndpoint
        );
    }


    /**
     * Punkt końcowy rozpatrujący zgłoszenie użytkownika o podanym id
     *
     * @param reportId identyfikator zgłoszenia użytkownika
     * @return odpowiedź HTTP
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @POST
    @Path("/account/{id}/resolve")
    public Response resolveAccountReport(@NotNull @PathParam("id") Long reportId) throws BaseApplicationException {
        repeat(() -> reportEndpoint.resolveAccountReport(reportId), reportEndpoint);
        return Response.status(Response.Status.OK).build();
    }

    /**
     * Punkt końcowy rozpatrujący zgłoszenie fotografa o podanym id
     *
     * @param photographerId identyfikator recenzji fotografa
     * @return odpowiedź HTTP
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @POST
    @Path("/photographer/{id}/resolve")
    public Response resolvePhotographerReport(@NotNull @PathParam("id") Long photographerId) throws BaseApplicationException {
        repeat(() -> reportEndpoint.resolvePhotographerReport(photographerId), reportEndpoint);
        return Response.status(Response.Status.OK).build();
    }

    /**
     * Punkt końcowy rozpatrujący zgłoszenie recenzji o podanym id
     *
     * @param reviewId identyfikator recenzji
     * @return odpowiedź HTTP
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @POST
    @Path("/review/{id}/resolve")
    public Response resolveReviewReport(@NotNull @PathParam("id") Long reviewId) throws BaseApplicationException {
        repeat(() -> reportEndpoint.resolveReviewReport(reviewId), reportEndpoint);
        return Response.status(Response.Status.OK).build();
    }
}
