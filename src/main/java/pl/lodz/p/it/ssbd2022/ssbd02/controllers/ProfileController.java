package pl.lodz.p.it.ssbd2022.ssbd02.controllers;

import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.*;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.dto.AddPhotoDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.dto.CreateReviewDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.endpoint.PhotoEndpoint;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.endpoint.ProfileEndpoint;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.endpoint.ReviewEndpoint;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/profile")
public class ProfileController extends AbstractController {

    @Inject
    PhotoEndpoint photoEndpoint;

    @Inject
    ProfileEndpoint photographerEndpoint;

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

    @DELETE
    @Path("/photo/{id}")
    public Response deletePhotoFromGallery(@PathParam("id") Long photoId) throws NoAuthenticatedAccountFound, NoPhotoFoundException {
        throw new UnsupportedOperationException();
    }

    @POST
    @Path("/photo/{id}/like")
    public Response likePhoto(@PathParam("id") Long photoId) throws BaseApplicationException {
        repeat(() -> photoEndpoint.likePhoto(photoId), photoEndpoint);
        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Path("/photo/{id}/unlike")
    public Response unlikePhoto(@PathParam("id") Long photoId) throws NoAuthenticatedAccountFound, NoPhotoFoundException {
        throw new UnsupportedOperationException();
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
    public Response unlikeReview(@PathParam("id") Long reviewId) throws NoReviewFoundException, NoAuthenticatedAccountFound {
        throw new UnsupportedOperationException();
    }
}
