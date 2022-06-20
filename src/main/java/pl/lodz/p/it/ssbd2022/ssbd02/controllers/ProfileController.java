package pl.lodz.p.it.ssbd2022.ssbd02.controllers;

import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.*;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.dto.*;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.dto.AddPhotoDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.dto.CreateReviewDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.dto.GetReviewDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.dto.ReviewDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.endpoint.PhotoEndpoint;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.endpoint.ProfileEndpoint;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.endpoint.ReviewEndpoint;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/profile")
public class ProfileController extends AbstractController {

    @Inject
    PhotoEndpoint photoEndpoint;

    @Inject
    ProfileEndpoint profileEndpoint;

    @Inject
    ReviewEndpoint reviewEndpoint;


    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/description")
    public Response changeDescription(@NotNull @Valid String newDescription) throws NoAuthenticatedAccountFound {
        throw new UnsupportedOperationException();
    }

    /**
     * Dodaje nowe zdjęcie do galerii obecnie uwierzytelnionego fotografa
     *
     * @param addPhotoDto obiekt DTO zawierający informacje potrzebne do dodania zdjęcia
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/photo")
    public void addPhotoToGallery(@NotNull @Valid AddPhotoDto addPhotoDto) throws BaseApplicationException {
        repeat(() -> photoEndpoint.addPhotoToGallery(addPhotoDto), photoEndpoint);
    }

    /**
     * Usuwa zdjęcie o podanym identyfikatorze z galerii fotografa
     *
     * @param photoId identyfikator zdjęcia, które ma zostać usunięte
     * @throws BaseApplicationException przy niepowodzeniu operacji
     */
    @DELETE
    @Path("/photo/{id}")
    public void deletePhotoFromGallery(@PathParam("id") Long photoId) throws BaseApplicationException {
        repeat(() -> photoEndpoint.deletePhotoFromGallery(photoId), photoEndpoint);
    }

    @POST
    @Path("/photo/{id}/like")
    public Response likePhoto(@PathParam("id") Long photoId) throws BaseApplicationException {
        repeat(() -> photoEndpoint.likePhoto(photoId), photoEndpoint);
        return Response.status(Response.Status.OK).build();
    }

    /**
     * Usuwa polubienie na wybranym zdjęciu
     *
     * @param photoId Id zdjęcia
     * @throws BaseApplicationException W przypadku niepowodzenia akcji
     */
    @POST
    @Path("/photo/{id}/unlike")
    public Response unlikePhoto(@PathParam("id") Long photoId) throws BaseApplicationException {
        repeat(() -> photoEndpoint.unlikePhoto(photoId), photoEndpoint);
        return Response.status(Response.Status.OK).build();
    }

    /**
     * Dodaje recenzję fotografowi
     *
     * @param review obiekt DTO zawierający login fotografa, ocenę w skali od 1 do 10 i słowną opinię
     * @throws BaseApplicationException przy niepowodzeniu operacji
     */
    @POST
    @Path("/review")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response reviewPhotographer(@NotNull @Valid CreateReviewDto review)
            throws BaseApplicationException {
        repeat(() -> reviewEndpoint.reviewPhotographer(review), reviewEndpoint);
        return Response.status(Response.Status.OK).build();
    }

    @DELETE
    @Path("/review/{id}")
    public Response deleteOwnPhotographerReview(@PathParam("id") Long reviewId)
            throws BaseApplicationException {
        repeat(() -> reviewEndpoint.deleteOwnPhotographerReview(reviewId), reviewEndpoint);
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("/review/{id}")
    public GetReviewDto getReviewById(@PathParam("id") Long reviewId)
            throws BaseApplicationException {
        return repeat(() -> reviewEndpoint.getReviewById(reviewId), reviewEndpoint);
    }

    @DELETE
    @Path("/review/{id}/admin")
    public Response deleteSomeonesPhotographerReview(@PathParam("id") Long reviewId)
            throws BaseApplicationException {
        repeat(() -> reviewEndpoint.deleteSomeonesPhotographerReview(reviewId), reviewEndpoint);
        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Path("/review/{id}/like")
    public Response likeReview(@PathParam("id") Long reviewId) throws BaseApplicationException {
        repeat(() -> reviewEndpoint.likeReview(reviewId), reviewEndpoint);
        return Response.accepted().build();
    }

    @POST
    @Path("/review/{id}/unlike")
    public Response unlikeReview(@PathParam("id") Long reviewId) throws BaseApplicationException {
        repeat(() -> reviewEndpoint.unlikeReview(reviewId), reviewEndpoint);
        return Response.accepted().build();
    }

    /**
     * Punkt końcowy zwracający listę recenzji dla danego fotografa
     *
     * @param pageNo            numer strony do pobrania
     * @param recordsPerPage    liczba rekordów na stronie
     * @param photographerLogin login fotografa którego dotyczą recenzje
     * @return lista recenzji
     * @throws WrongParameterException w przypadku gdy podano złą nazwę kolumny lub kolejność sortowania
     */
    @GET
    @Path("review/list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ListResponseDto<ReviewDto> getReviewList(
            @QueryParam("pageNo") @DefaultValue("1") int pageNo,
            @QueryParam("recordsPerPage") @DefaultValue("1") int recordsPerPage,
            @QueryParam("photographerLogin") @NotNull String photographerLogin
    ) throws BaseApplicationException {
        return repeat(() -> reviewEndpoint.getReviewsByPhotographerLogin(pageNo, recordsPerPage, photographerLogin), reviewEndpoint);
    }

    /**
     * Punkt końcowy zwracający listę zdjęc danego fotografa
     *
     * @param pageNo            numer strony do pobrania
     * @param recordsPerPage    liczba rekordów na stronie
     * @param photographerLogin login fotografa
     * @return lista recenzji
     * @throws WrongParameterException w przypadku gdy podano złą nazwę kolumny lub kolejność sortowania
     */
    @GET
    @Path("photo/list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ListResponseDto<PhotoDto> getPhotoList(
            @QueryParam("pageNo") @DefaultValue("1") int pageNo,
            @QueryParam("recordsPerPage") @DefaultValue("10") int recordsPerPage,
            @QueryParam("photographerLogin") @NotNull String photographerLogin
    ) throws BaseApplicationException {
        return repeat(() -> photoEndpoint.getPhotoList(photographerLogin, pageNo, recordsPerPage), reviewEndpoint);
    }


    /**
     * Punkt końcowy pozwalający edytować listę specjalizacji fotografa
     *
     * @param specializations lista specjalizacji
     * @return status
     * @throws BaseApplicationException przy niepowodzeniu operacji
     */
    @PUT
    @Path("/specializations")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateSpecializations(@NotNull @Valid List<String> specializations)
            throws BaseApplicationException {
        repeat(() -> profileEndpoint.changeSpecializations(specializations), profileEndpoint);
        return Response.status(Response.Status.OK).build();
    }

    /**
     * Punkt końcowy zwracający listę specjalizacji fotografa
     *
     * @return lista specjalizacji
     * @throws BaseApplicationException przy niepowodzeniu operacji
     */
    @GET
    @Path("/specializations")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getSpecializations() throws BaseApplicationException {
        return repeat(() -> profileEndpoint.getOwnSpecializations(), profileEndpoint);
    }

    /**
     * Punkt końcowy zwracający listę wszystkich dostępnych specjalizacji
     *
     * @return lista specjalizacji
     * @throws BaseApplicationException przy niepowodzeniu operacji
     */
    @GET
    @Path("/specialization-list")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> getAllSpecializations() throws BaseApplicationException {
        return repeat(() -> profileEndpoint.getAllSpecializations(), profileEndpoint);
    }
}
