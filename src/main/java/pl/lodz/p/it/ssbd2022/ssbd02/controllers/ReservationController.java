package pl.lodz.p.it.ssbd2022.ssbd02.controllers;

import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.dto.*;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.endpoint.ReservationEndpoint;
import pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint.NameSurnameQuery;
import pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint.Order;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/reservation")
public class ReservationController extends AbstractController {

    @Inject
    private ReservationEndpoint reservationEndpoint;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createReservation(
            @NotNull @Valid CreateReservationDto createReservationDto
    ) throws BaseApplicationException {
        repeat(() -> reservationEndpoint.createReservation(createReservationDto), reservationEndpoint);
        return Response.status(Response.Status.CREATED).build();
    }

    @DELETE
    @Path("/{id}/cancel")
    public Response cancelReservation(
            @NotNull @PathParam("id") Long reservationId
    ) throws BaseApplicationException {
        repeat(() -> reservationEndpoint.cancelReservation(reservationId), reservationEndpoint);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}/discard")
    public Response discardReservation(
            @NotNull @PathParam("id") Long reservationId
    ) throws BaseApplicationException {
        repeat(() -> reservationEndpoint.discardReservation(reservationId), reservationEndpoint);
        return Response.ok().build();
    }

    /**
     * Punkt końcowy pozwalający na pobieranie rezerwacji dla użytkownika (niezakończonych lub wszystkich)
     *
     * @param name           imię lub nazwisko do wyszukania
     * @param pageNo         numer strony
     * @param recordsPerPage liczba recenzji na stronę
     * @param order          kolejność sortowania względem kolumny time_from
     * @param getAll         flaga decydująca o tym, czy pobierane są wszystkie rekordy, czy tylko niezakończone
     * @return ReservationListEntryDto      lista rezerwacji
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @GET
    @Path("/my-reservations")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ReservationListEntryDto> listReservations(
            @NameSurnameQuery @QueryParam("name") String name,
            @QueryParam("pageNo") @DefaultValue("1") Integer pageNo,
            @QueryParam("recordsPerPage") @NotNull Integer recordsPerPage,
            @QueryParam("order") @Order @DefaultValue("asc") String order,
            @QueryParam("all") @DefaultValue("false") Boolean getAll
    ) throws BaseApplicationException {
        return reservationEndpoint.listReservations(name, pageNo, recordsPerPage, order, getAll);
    }

    /**
     * Metoda pozwalająca na pobieranie rezerwacji dla fotografa (niezakończonych lub wszystkich)
     *
     * @param name           imię lub nazwisko do wyszukania
     * @param pageNo         numer strony
     * @param recordsPerPage liczba recenzji na stronę
     * @param order          kolejność sortowania względem kolumny time_from
     * @param getAll         flaga decydująca o tym, czy pobierane są wszystkie rekordy, czy tylko niezakończone
     * @return ReservationListEntryDto      lista rezerwacji
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @GET
    @Path("/my-jobs")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ReservationListEntryDto> listJobs(
            @NameSurnameQuery @QueryParam("name") String name,
            @QueryParam("pageNo") @DefaultValue("1") Integer pageNo,
            @QueryParam("recordsPerPage") @NotNull Integer recordsPerPage,
            @QueryParam("order") @Order @DefaultValue("asc") String order,
            @QueryParam("all") @DefaultValue("false") Boolean getAll
    ) throws BaseApplicationException {
        return reservationEndpoint.listJobs(name, pageNo, recordsPerPage, order, getAll);
    }

    /**
     * Punkt końcowy pozwalający na uzyskanie stronicowanej listy wszystkich aktywnych w systemie fotografów
     *
     * @param page           strona listy, którą należy pozyskać
     * @param recordsPerPage ilość krotek fotografów na stronie
     * @return stronicowana lista aktywnych fotografów obecnych systemie
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @GET
    @Path("/photographers")
    @Produces(MediaType.APPLICATION_JSON)
    public MorListResponseDto<PhotographerListEntryDto> listPhotographers(
            @QueryParam("pageNo") @DefaultValue("1") Integer page,
            @QueryParam("recordsPerPage") @DefaultValue("25") Integer recordsPerPage
    ) throws BaseApplicationException {
        return reservationEndpoint.listPhotographers(page, recordsPerPage);
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
