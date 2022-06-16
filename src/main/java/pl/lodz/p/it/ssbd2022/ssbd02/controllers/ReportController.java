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

@Path("/report")
public class ReportController extends AbstractController {

    @Inject
    ReportEndpoint reportEndpoint;

    @POST
    @Path("/account")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response reportAccount(@NotNull @Valid CreateAccountReportDto createAccountReportDto)
            throws NoAuthenticatedAccountFound, NoAccountFound {
        throw new UnsupportedOperationException();
    }

    @POST
    @Path("/photographer")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response reportPhotographer(@NotNull @Valid CreatePhotographerReportDto createPhotographerReportDto)
            throws NoAuthenticatedAccountFound, NoPhotoFoundException {
        throw new UnsupportedOperationException();
    }

    @POST
    @Path("/review")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response reportReview(@NotNull @Valid CreateReviewReportDto createReviewReportDto)
            throws BaseApplicationException {
        repeat(() -> reportEndpoint.reportReview(createReviewReportDto), reportEndpoint);
        return Response.status(Response.Status.CREATED).build();
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
    public Response resolveAccountReport(@PathParam("id") Long reportId) {
        throw new UnsupportedOperationException();
    }

    @POST
    @Path("/photographer/{id}/resolve")
    public Response resolvePhotographerReport(@PathParam("id") Long photographerId) {
        throw new UnsupportedOperationException();
    }

    @POST
    @Path("/review/{id}/resolve")
    public Response resolveReviewReport(@PathParam("id") Long reviewId) {
        throw new UnsupportedOperationException();
    }
}
