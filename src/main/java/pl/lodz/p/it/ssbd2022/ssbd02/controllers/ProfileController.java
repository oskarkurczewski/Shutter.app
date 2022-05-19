package pl.lodz.p.it.ssbd2022.ssbd02.controllers;

import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAuthenticatedAccountFound;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoPhotoFoundException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoPhotographerFoundException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoReviewFoundException;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.dto.AddPhotoDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.dto.CreateReviewDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.endpoint.PhotoEndpoint;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.endpoint.PhotographerEndpoint;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.endpoint.ReviewEndpoint;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("profile")
public class ProfileController extends AbstractContoller {

    @Inject
    PhotoEndpoint photoEndpoint;

    @Inject
    PhotographerEndpoint photographerEndpoint;

    @Inject
    ReviewEndpoint reviewEndpoint;


    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/description")
    public Response changeDescription(@NotNull @Valid String newDescription) throws NoAuthenticatedAccountFound {
        throw new UnsupportedOperationException();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/photo")
    public Response addPhotoToGallery(@NotNull @Valid AddPhotoDto addPhotoDto) throws NoAuthenticatedAccountFound {
        throw new UnsupportedOperationException();
    }

    @DELETE
    @Path("/photo/{id}")
    public Response deletePhotoFromGallery(@PathParam("id") Long photoId) throws NoAuthenticatedAccountFound, NoPhotoFoundException {
        throw new UnsupportedOperationException();
    }
//
    @POST
    @Path("/photo/{id}/like")
    public Response likePhoto(@PathParam("id") Long photoId) throws NoAuthenticatedAccountFound, NoPhotoFoundException {
        throw new UnsupportedOperationException();
    }

    @POST
    @Path("/photo/{id}/unlike")
    public Response unlikePhoto(@PathParam("id") Long photoId) throws NoAuthenticatedAccountFound, NoPhotoFoundException {
        throw new UnsupportedOperationException();
    }

    @POST
    @Path("/review")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response reviewPhotographer(@NotNull @Valid CreateReviewDto review)
            throws NoAuthenticatedAccountFound, NoPhotographerFoundException {
        throw new UnsupportedOperationException();
    }

    @DELETE
    @Path("/review/{id}")
    public Response deleteOwnPhotographerReview(@PathParam("id") Long reviewId)
            throws NoAuthenticatedAccountFound, NoReviewFoundException {
        throw new UnsupportedOperationException();
    }

    @DELETE
    @Path("/review/{id}/admin")
    public Response deleteSomeonesPhotographerReview(@PathParam("id") Long reviewId)
            throws NoReviewFoundException {
        throw new UnsupportedOperationException();
    }

    @POST
    @Path("/review/{id}/like")
    public Response likeReview(@PathParam("id") Long reviewId) throws NoReviewFoundException, NoAuthenticatedAccountFound {
        throw new UnsupportedOperationException();
    }

    @POST
    @Path("/review/{id}/unlike")
    public Response unlikeReview(@PathParam("id") Long reviewId) throws NoReviewFoundException, NoAuthenticatedAccountFound {
        throw new UnsupportedOperationException();
    }
}
