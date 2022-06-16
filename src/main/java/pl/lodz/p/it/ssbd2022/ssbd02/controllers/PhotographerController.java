package pl.lodz.p.it.ssbd2022.ssbd02.controllers;

import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAuthenticatedAccountFound;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoPhotographerFound;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.dto.BasePhotographerInfoDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.dto.ChangeDescriptionDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.dto.DetailedPhotographerInfoDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.endpoint.PhotographerEndpoint;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.endpoint.ProfileEndpoint;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.endpoint.ReportEndpoint;
import pl.lodz.p.it.ssbd2022.ssbd02.security.etag.SignatureVerifier;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/photographer")
public class PhotographerController extends AbstractController {

    @Inject
    private PhotographerEndpoint photographerEndpoint;

    @Inject
    private SignatureVerifier signatureVerifier;

    @Inject
    private ReportEndpoint reportEndpoint;

    @Inject
    private ProfileEndpoint profileEndpoint;

    /**
     * Punkt końcowy szukający fotografa
     *
     * @param login Login użytkownika fotografa
     * @throws NoPhotographerFound         W przypadku gdy fotograf o podanej nazwie użytkownika nie istnieje,
     *                                     gdy konto szukanego fotografa jest nieaktywne, niepotwierdzone lub profil nieaktywny i informacje próbuje uzyskać użytkownik
     *                                     niebędący ani administratorem, ani moderatorem
     * @throws NoAuthenticatedAccountFound W przypadku gdy dane próbuje uzyskać niezalogowana osoba
     * @see BasePhotographerInfoDto
     */
    @GET
    @Path("/{login}/info")
    @Produces(MediaType.APPLICATION_JSON)
    public BasePhotographerInfoDto getPhotographerInfo(@NotNull @PathParam("login") String login) throws BaseApplicationException {
        return repeat(() -> photographerEndpoint.getPhotographerInfo(login), photographerEndpoint);
    }

    /**
     * Punkt końcowy szukający fotografa
     *
     * @param login Login użytkownika fotografa
     * @throws NoPhotographerFound         W przypadku gdy fotograf o podanej nazwie użytkownika nie istnieje,
     *                                     gdy konto szukanego fotografa jest nieaktywne, niepotwierdzone lub profil nieaktywny i informacje próbuje uzyskać użytkownik
     *                                     niebędący ani administratorem, ani moderatorem
     * @throws NoAuthenticatedAccountFound W przypadku gdy dane próbuje uzyskać niezalogowana osoba
     * @see BasePhotographerInfoDto
     */
    @GET
    @Path("/{login}/detailed-info")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEnhancedPhotographerInfo(@NotNull @PathParam("login") String login) throws BaseApplicationException {
        DetailedPhotographerInfoDto DetailedPhotographerInfoDto = repeat(() -> photographerEndpoint.getEnhancedPhotographerInfo(login), photographerEndpoint);
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
        DetailedPhotographerInfoDto DetailedPhotographerInfoDto = repeat(() -> photographerEndpoint.getYourPhotographerInfo(), photographerEndpoint);
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
    public Response editDescription(@NotNull @Valid ChangeDescriptionDto changeDescriptionDto) throws BaseApplicationException {
        repeat(() -> profileEndpoint.changeDescription(changeDescriptionDto), profileEndpoint);
        return Response.accepted().build();
    }

}
