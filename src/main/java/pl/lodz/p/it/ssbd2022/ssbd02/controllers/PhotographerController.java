package pl.lodz.p.it.ssbd2022.ssbd02.controllers;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.WeekDay;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.ExceptionFactory;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAuthenticatedAccountFound;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoPhotographerFound;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.dto.MorListResponseDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.dto.PhotographerListEntryDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.endpoint.ReservationEndpoint;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.dto.BasePhotographerInfoDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.dto.ChangeDescriptionDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.dto.DetailedPhotographerInfoDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.endpoint.PhotographerEndpoint;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.endpoint.ProfileEndpoint;
import pl.lodz.p.it.ssbd2022.ssbd02.security.etag.SignatureVerifier;
import pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint.Login;
import pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint.NameSurnameQuery;
import pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint.NumberQuery;
import pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint.Specialization;
import pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint.Time;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalTime;

@Path("/photographer")
public class PhotographerController extends AbstractController {

    @Inject
    private PhotographerEndpoint photographerEndpoint;

    @Inject
    ReservationEndpoint reservationEndpoint;

    @Inject
    private SignatureVerifier signatureVerifier;

    @Inject
    private ProfileEndpoint profileEndpoint;

    /**
     * Punkt końcowy szukający fotografa
     *
     * @param login Login użytkownika fotografa
     * @throws NoPhotographerFound         W przypadku gdy fotograf o podanej nazwie użytkownika nie istnieje,
     *                                     gdy konto szukanego fotografa jest nieaktywne, niepotwierdzone lub
     *                                     profil nieaktywny i informacje próbuje uzyskać użytkownik
     *                                     niebędący ani administratorem, ani moderatorem
     * @throws NoAuthenticatedAccountFound W przypadku gdy dane próbuje uzyskać niezalogowana osoba
     * @see BasePhotographerInfoDto
     */
    @GET
    @Path("/{login}/info")
    @Produces(MediaType.APPLICATION_JSON)
    public BasePhotographerInfoDto getPhotographerInfo(@NotNull @Login @PathParam("login") String login)
            throws BaseApplicationException {
        return repeat(() -> photographerEndpoint.getPhotographerInfo(login), photographerEndpoint);
    }

    /**
     * Punkt końcowy szukający fotografa
     *
     * @param login Login użytkownika fotografa
     * @throws NoPhotographerFound         W przypadku gdy fotograf o podanej nazwie użytkownika nie istnieje,
     *                                     gdy konto szukanego fotografa jest nieaktywne, niepotwierdzone lub profil
     *                                     nieaktywny i informacje próbuje uzyskać użytkownik
     *                                     niebędący ani administratorem, ani moderatorem
     * @throws NoAuthenticatedAccountFound W przypadku gdy dane próbuje uzyskać niezalogowana osoba
     * @see BasePhotographerInfoDto
     */
    @GET
    @Path("/{login}/detailed-info")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEnhancedPhotographerInfo(@NotNull @Login @PathParam("login") String login)
            throws BaseApplicationException {
        DetailedPhotographerInfoDto DetailedPhotographerInfoDto = repeat(
                () -> photographerEndpoint.getEnhancedPhotographerInfo(login),
                photographerEndpoint
        );
        EntityTag tag = new EntityTag(signatureVerifier.calculateEntitySignature(DetailedPhotographerInfoDto));
        return Response.status(Response.Status.ACCEPTED).entity(DetailedPhotographerInfoDto).tag(tag).build();

    }

    /**
     * Punkt końcowy zwracający informacje o zalogowanym fotografie
     *
     * @throws NoPhotographerFound         W przypadku gdy profil fotografa dla użytkownika nie istnieje
     * @throws NoAuthenticatedAccountFound W przypadku gdy dane próbuje uzyskać niezalogowana osoba
     * @see DetailedPhotographerInfoDto
     */
    @GET
    @Path("/info")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPhotographerInfo() throws BaseApplicationException {
        DetailedPhotographerInfoDto DetailedPhotographerInfoDto = repeat(
                () -> photographerEndpoint.getYourPhotographerInfo(),
                photographerEndpoint
        );
        EntityTag tag = new EntityTag(signatureVerifier.calculateEntitySignature(DetailedPhotographerInfoDto));
        return Response.status(Response.Status.ACCEPTED).entity(DetailedPhotographerInfoDto).tag(tag).build();
    }

    /**
     * Punkt końcowy pozwalający uwierzytelnionemu fotografowi zmienić opis na swoim profilu
     *
     * @param changeDescriptionDto obiekt DTO zawierający nowy opis
     * @throws NoPhotographerFound         W przypadku gdy profil fotografa dla użytkownika nie istnieje
     * @throws NoAuthenticatedAccountFound Gdy użytkownik nie jest uwierzytelniony
     * @throws BaseApplicationException    gdy aktualizacja opisu się nie powiedzie
     */
    @PUT
    @Path("/change-description")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editDescription(@NotNull @Valid ChangeDescriptionDto changeDescriptionDto)
            throws BaseApplicationException {
        repeat(() -> profileEndpoint.changeDescription(changeDescriptionDto), profileEndpoint);
        return Response.accepted().build();
    }

    /**
     * Punkt końcowy pozwalający na uzyskanie stronicowanej listy wszystkich aktywnych w systemie fotografów, których
     * imię lub nazwisko zawiera szukaną frazę
     * Opcjonalnie szuka również po specjalizacji
     *
     * @param name           szukana fraza
     * @param page           strona listy, którą należy pozyskać
     * @param recordsPerPage ilość krotek fotografów na stronie
     * @param spec           specjalizacja
     * @param weekDay        dzień tygodnia, w którym szukani są fotografowie
     * @param from           godzina, od której szukani są fotografowie w formacie HH:mm
     * @param to             godzina, do której szukani są fotografowie w formacie HH:mm
     * @return stronicowana lista aktywnych fotografów obecnych
     * systemie, których imię lub nazwisko zawiera podaną frazę
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @GET
    @Path("/by-name-surname")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPhotographerByNameSurnameSpecializationWeekDayFromTimeEndTime(
            @NameSurnameQuery @QueryParam("name") String name,
            @NumberQuery @QueryParam("pageNo") @DefaultValue("1") Integer page,
            @NumberQuery @QueryParam("recordsPerPage") @DefaultValue("25") Integer recordsPerPage,
            @Specialization @QueryParam("specialization") String spec,
            @QueryParam("weekDay") String weekDay,
            @Time @QueryParam("from") String from,
            @Time @QueryParam("to") String to
    ) throws BaseApplicationException {
        LocalTime fromTime = from == null ? null : LocalTime.of(Integer.parseInt(from.split(":")[0]), Integer.parseInt(from.split(":")[1]));
        LocalTime toTime = to == null ? null : LocalTime.of(Integer.parseInt(to.split(":")[0]), Integer.parseInt(to.split(":")[1]));
        MorListResponseDto<PhotographerListEntryDto> responseDto = repeat(
            () -> reservationEndpoint.findPhotographerByNameSurnameSpecializationWeekDayFromTimeEndTime(
                name,
                page,
                recordsPerPage,
                spec,
                weekDay,
                fromTime,
                toTime
        ), reservationEndpoint);
        return Response.status(Response.Status.OK).entity(responseDto).build();
    }
}
