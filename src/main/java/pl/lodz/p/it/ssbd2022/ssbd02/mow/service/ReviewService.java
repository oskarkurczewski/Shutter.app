package pl.lodz.p.it.ssbd2022.ssbd02.mow.service;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Review;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoReviewFoundException;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.*;

@Stateless
public class ReviewService {

    @PermitAll
    public void findById(Long id) throws NoReviewFoundException{
        throw new UnsupportedOperationException();
    }

    @RolesAllowed({deleteOwnPhotographerReview, deleteSomeonesPhotographerReview})
    public void deletePhotographerReview(Review review) {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(reviewPhotographer)
    public void addPhotographerReview(Review review) {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(likeReview)
    public void likeReview(Account account, Review review) {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(unlikeReview)
    public void unlikeReview(Account account, Review review) {
        throw new UnsupportedOperationException();
    }
}
