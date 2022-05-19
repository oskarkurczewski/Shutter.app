package pl.lodz.p.it.ssbd2022.ssbd02.mow.endpoint;

import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAuthenticatedAccountFound;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoPhotographerFoundException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoReviewFoundException;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.dto.CreateReviewDto;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.*;

@Stateless
public class ReviewEndpoint {

    @RolesAllowed(reviewPhotographer)
    public void reviewPhotographer(CreateReviewDto review)
            throws NoAuthenticatedAccountFound, NoPhotographerFoundException {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(deleteOwnPhotographerReview)
    public void deleteOwnPhotographerReview(Long reviewId)
            throws NoAuthenticatedAccountFound, NoReviewFoundException {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(deleteSomeonesPhotographerReview)
    public void deleteSomeonesPhotographerReview(Long reviewId)
            throws NoReviewFoundException {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(likeReview)
    public void likeReview(Long reviewId) throws NoReviewFoundException, NoAuthenticatedAccountFound {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(unlikeReview)
    public void unlikeReview(Long reviewId) throws NoReviewFoundException, NoAuthenticatedAccountFound {
        throw new UnsupportedOperationException();
    }
}
