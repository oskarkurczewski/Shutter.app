package pl.lodz.p.it.ssbd2022.ssbd02.mow.endpoint;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Review;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAuthenticatedAccountFound;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoPhotographerFoundException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoReviewFoundException;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.dto.ReviewDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.service.AccountService;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.dto.CreateReviewDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.service.PhotographerService;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.service.ReviewService;
import pl.lodz.p.it.ssbd2022.ssbd02.security.AuthenticationContext;
import pl.lodz.p.it.ssbd2022.ssbd02.util.AbstractEndpoint;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import java.util.ArrayList;
import java.util.List;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.*;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class ReviewEndpoint extends AbstractEndpoint {

    @Inject
    private ReviewService reviewService;
    @Inject
    private AccountService accountService;
    @Inject
    private AuthenticationContext authCtx;
    @Inject
    private PhotographerService photographerService;

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

    /**
     * Wykonuje polubienie danej recenzji przez zalogowanego użytkownika
     *
     * @param reviewId id recenzji mającej być polubioną
     * @throws BaseApplicationException
     */
    @RolesAllowed(likeReview)
    public void likeReview(Long reviewId) throws BaseApplicationException {
        Review review = reviewService.findById(reviewId);
        String user = authCtx.getCurrentUsersLogin();
        Account account = accountService.findByLogin(user);
        reviewService.likeReview(account, review);
    }

    @RolesAllowed(unlikeReview)
    public void unlikeReview(Long reviewId) throws NoReviewFoundException, NoAuthenticatedAccountFound {
        throw new UnsupportedOperationException();
    }

    @PermitAll
    public List<ReviewDto> getReviewsByPhotographerLogin(int pageNo, int recordsPerPage, String photographerLogin)
            throws BaseApplicationException {
        Long photographerId = photographerService.findByLogin(photographerLogin).getId();
        List<Review> reviews = reviewService.listReviewsByPhotographerId(pageNo, recordsPerPage, photographerId);
        List<ReviewDto> reviewDtoList = new ArrayList<>();

        for (Review review: reviews) {
            reviewDtoList.add(new ReviewDto(review));
        }

        return reviewDtoList;
    }
}
