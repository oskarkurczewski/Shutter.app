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
    ReportEndpoint reportEndpoint;

    @POST
    @Path("/account")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response reportAccount(@NotNull @Valid CreateAccountReportDto createAccountReportDto)
            throws NoAuthenticatedAccountFound, NoAccountFound {
        throw new UnsupportedOperationException();
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
