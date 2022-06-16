package pl.lodz.p.it.ssbd2022.ssbd02.controllers;

import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.*;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.dto.*;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.endpoint.ReportEndpoint;

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
            throws NoAuthenticatedAccountFound, NoReviewFoundException {
        throw new UnsupportedOperationException();
    }

    @GET
    @Path("/account")
    @Produces(MediaType.APPLICATION_JSON)
    public List<AccountReportDto> listAllAccountReports() {
        throw new UnsupportedOperationException();
    }

    @GET
    @Path("/photographer")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PhotographerReportDto> listAllPhotographerReports() {
        throw new UnsupportedOperationException();
    }

    @GET
    @Path("/review")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ReviewReportDto> listAllReviewReports() {
        throw new UnsupportedOperationException();
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
