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
     * Punkt końcowy pozwalający na zgłoszenie klienta z podanym powodem.
     *
     * @param createAccountReportDto Obiekt przedstawiający dane zawierające login zgłoszonego klienta oraz powód.
     * @throws BaseApplicationException W przypadku niepowodzenia operacji
     */
    @POST
    @Path("/account")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response reportClientAccount(@NotNull @Valid CreateAccountReportDto createAccountReportDto)
            throws BaseApplicationException {
        reportEndpoint.reportClientAccount(createAccountReportDto);
        return Response.status(Response.Status.OK).build();
    }

    /**
     * Punkt końcowy pozwalający zgłosić fotografa
     *
     * @param createPhotographerReportDto obiekt DTO zawierający dane zgłoszenia
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
     * @return the all photographer report causes
     * @throws BaseApplicationException the base application exception
     */
    @GET
    @Path("/photographer/report-causes")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getAllPhotographerReportCauses() throws BaseApplicationException {
        return repeat(() -> reportEndpoint.getAllPhotographerReportCauses(), reportEndpoint);
    }


    @POST
    @Path("/review")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response reportReview(@NotNull @Valid CreateReviewReportDto createReviewReportDto)
            throws BaseApplicationException {
        repeat(() -> reportEndpoint.reportReview(createReviewReportDto), reportEndpoint);
        return Response.status(Response.Status.CREATED).build();
    }
    
    @GET
    @Path("/review/report-causes")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getAllReviewReportCauses() throws BaseApplicationException {
        return repeat(() -> reportEndpoint.getAllReviewReportCauses(), reportEndpoint);
    }


    /**
     * Punkt końcowy zwracający listę zgłoszeń kont
     *
     * @return lista zgłoszeń kont
     * @throws WrongParameterException niepoprawna kolejność sortowania
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
     * Punkt końcowy zwracający listę zgłoszeń fotografów
     *
     * @return lista zgłoszeń fotografów
     * @throws WrongParameterException niepoprawna kolejność sortowania
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
     * @return lista zgłoszeń recenzji
     * @throws WrongParameterException niepoprawna kolejność sortowania
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


    @POST
    @Path("/account/{id}/resolve")
    public Response resolveAccountReport(@PathParam("id") Long reportId) throws BaseApplicationException {
        reportEndpoint.resolveAccountReport(reportId);
        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Path("/photographer/{id}/resolve")
    public Response resolvePhotographerReport(@PathParam("id") Long photographerId) throws BaseApplicationException {
        reportEndpoint.resolvePhotographerReport(photographerId);
        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Path("/review/{id}/resolve")
    public Response resolveReviewReport(@PathParam("id") Long reviewId) throws BaseApplicationException {
        reportEndpoint.resolveReviewReport(reviewId);
        return Response.status(Response.Status.OK).build();
    }
}
