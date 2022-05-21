package pl.lodz.p.it.ssbd2022.ssbd02.controllers;

import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.InvalidReservationTimeExcpetion;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoReservationFoundException;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.dto.CreateReservationDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.dto.PhotographerListEntryDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.dto.ReservationListEntryDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.dto.TimePeriodDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.endpoint.ReservationEndpoint;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

public class ReservationController extends AbstractController {

    @Inject
    private ReservationEndpoint reservationEndpoint;

    @POST
    @Path("/reservation")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createReservation(
            @NotNull @Valid CreateReservationDto createReservationDto
    ) throws InvalidReservationTimeExcpetion {
        throw new UnsupportedOperationException();
    }

    @DELETE
    @Path("/{id}/cancel")
    public Response cancelReservation(
            @NotNull @PathParam("id") Long reservationId
    ) throws NoReservationFoundException {
        throw new UnsupportedOperationException();
    }

    @DELETE
    @Path("/{id}/discard")
    public Response discardReservation(
            @NotNull @PathParam("id") Long reservationId
    ) throws NoReservationFoundException {
        throw new UnsupportedOperationException();
    }

    @GET
    @Path("/reservations")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ReservationListEntryDto> listReservations() {
        throw new UnsupportedOperationException();
    }

    @GET
    @Path("/jobs")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ReservationListEntryDto> listJobs() {
        throw new UnsupportedOperationException();
    }

    @GET
    @Path("/photographers")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PhotographerListEntryDto> listPhotographers() {
        throw new UnsupportedOperationException();
    }

    @GET
    @Path("/photographers/find/byAvailability")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<PhotographerListEntryDto> findPhotographerByAvailability(@NotNull @Valid TimePeriodDto timePeriod) {
        throw new UnsupportedOperationException();
    }

    @GET
    @Path("/photographers/find/byName")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<PhotographerListEntryDto> findPhotographerByName(@NotNull @Valid String name) {
        throw new UnsupportedOperationException();
    }

    @GET
    @Path("/photographers/find/bySpecialization")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<PhotographerListEntryDto> findPhotographerBySpeciality(@NotNull @Valid String specialization) {
        throw new UnsupportedOperationException();
    }
}
