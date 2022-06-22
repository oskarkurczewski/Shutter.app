package pl.lodz.p.it.ssbd2022.ssbd02.controllers;

import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.ExceptionFactory;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.dto.*;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.endpoint.ReservationEndpoint;
import pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint.Login;
import pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint.NameSurnameQuery;
import pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint.Order;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Path("/reservation")
public class ReservationController extends AbstractController {

    @Inject
    private ReservationEndpoint reservationEndpoint;

    /**
     * Punkt końcowy tworzący rezerwacją fotografa o podanych parametrach
     *
     * @param createReservationDto informacje o rezerwacji
     * @return odpowiedź HTTP
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createReservation(
            @NotNull @Valid CreateReservationDto createReservationDto
    ) throws BaseApplicationException {
        repeat(() -> reservationEndpoint.createReservation(createReservationDto), reservationEndpoint);
        return Response.status(Response.Status.CREATED).build();
    }

    /**
     * Punkt końcowy anulujący rezerwację o podanym identyfikatorze
     *
     * @param reservationId identyfikator rezerwacji
     * @return odpowiedź HTTP
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @DELETE
    @Path("/{id}/cancel")
    public Response cancelReservation(
            @NotNull @PathParam("id") Long reservationId
    ) throws BaseApplicationException {
        repeat(() -> reservationEndpoint.cancelReservation(reservationId), reservationEndpoint);
        return Response.ok().build();
    }

    /**
     * Punkt końcowy odrzucający rezerwację o podanym identyfikatorze
     *
     * @param reservationId identyfikator rezerwacji
     * @return odpowiedź HTTP
     * @throws BaseApplicationException niepowodzenie operacji
     */
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
     * @param name   imię lub nazwisko do wyszukania
     * @param order  kolejność sortowania względem kolumny time_from
     * @param getAll flaga decydująca o tym, czy pobierane są wszystkie rekordy, czy tylko niezakończone
     * @param date   poniedziałek dla tygodnia, dla którego mają być pobrane rezerwacje
     * @return ReservationListEntryDto      lista rezerwacji
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @GET
    @Path("/my-reservations")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ReservationListEntryDto> listReservations(
            @NameSurnameQuery @QueryParam("name") String name,
            @QueryParam("order") @Order @DefaultValue("asc") String order,
            @QueryParam("all") @DefaultValue("false") Boolean getAll,
            @NotNull @QueryParam("date") String date
    ) throws BaseApplicationException {
        LocalDate localDate = null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            localDate = LocalDate.parse(date, formatter);
            if (!localDate.getDayOfWeek().equals(DayOfWeek.MONDAY)) {
                throw ExceptionFactory.wrongDateException();
            }
        } catch (Exception e) {
            throw ExceptionFactory.wrongDateException();
        }
        return reservationEndpoint.listReservations(name, order, getAll, localDate);
    }

    /**
     * Punkt końcowy pozwalający na pobieranie rezerwacji dla fotografa (niezakończonych lub wszystkich)
     *
     * @param name   imię lub nazwisko do wyszukania
     * @param order  kolejność sortowania względem kolumny time_from
     * @param getAll flaga decydująca o tym, czy pobierane są wszystkie rekordy, czy tylko niezakończone
     * @param date   poniedziałek dla tygodnia, dla którego mają być pobrane rezerwacje
     * @return ReservationListEntryDto      lista rezerwacji
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @GET
    @Path("/my-jobs")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ReservationListEntryDto> listJobs(
            @NameSurnameQuery @QueryParam("name") String name,
            @QueryParam("order") @Order @DefaultValue("asc") String order,
            @QueryParam("all") @DefaultValue("false") Boolean getAll,
            @NotNull @QueryParam("date") String date
    ) throws BaseApplicationException {
        LocalDate localDate = null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            localDate = LocalDate.parse(date, formatter);
            if (!localDate.getDayOfWeek().equals(DayOfWeek.MONDAY)) {
                throw ExceptionFactory.wrongDateException();
            }
        } catch (Exception e) {
            throw ExceptionFactory.wrongDateException();
        }
        return reservationEndpoint.listJobs(name, order, getAll, localDate);
    }

    /**
     * Metoda pozwalająca na pobieranie rezerwacji dla fotografa. Służy do wyświetlania danych w kalendarzu
     *
     * @param date              poniedziałek dla tygodnia, dla którego mają być pobrane rezerwacje
     * @param photographerLogin login fotografa
     * @return ReservationListEntryDto      lista rezerwacji
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @GET
    @Path("/{login}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ReservationCalendarEntryDto> listPhotographerJobs(
            @NotNull @Login @PathParam("login") String photographerLogin,
            @NotNull @QueryParam("date") String date
    ) throws BaseApplicationException {
        LocalDate localDate = null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            localDate = LocalDate.parse(date, formatter);
            if (!localDate.getDayOfWeek().equals(DayOfWeek.MONDAY)) {
                throw ExceptionFactory.wrongDateException();
            }
        } catch (Exception e) {
            throw ExceptionFactory.wrongDateException();
        }
        return reservationEndpoint.listPhotographerJobs(photographerLogin, localDate);
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
        return repeat(() -> reservationEndpoint.listPhotographers(page, recordsPerPage), reservationEndpoint);
    }
}
