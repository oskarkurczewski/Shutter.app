package pl.lodz.p.it.ssbd2022.ssbd02.mow.endpoint;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.PhotographerInfo;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Review;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.*;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.service.AccountService;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.dto.CreateReviewDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.service.ProfileService;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.service.ReviewService;
import pl.lodz.p.it.ssbd2022.ssbd02.security.AuthenticationContext;
import pl.lodz.p.it.ssbd2022.ssbd02.util.AbstractEndpoint;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.ws.rs.NotAllowedException;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.*;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class ReviewEndpoint extends AbstractEndpoint {

    @Inject
    private ReviewService reviewService;
    @Inject
    private AccountService accountService;
    @Inject
    private ProfileService profileService;
    @Inject
    private AuthenticationContext authCtx;

    /**
     * Dodaje recenzje przez zalogowanego użytkownika
     * @param review obiekt DTO zawierający login fotografa, ocenę w skali od 1 do 10 i słowną opinię
     * @throws BaseApplicationException w przypadku niepowodzenia operacji
     */
    @RolesAllowed(reviewPhotographer)
    public void reviewPhotographer(CreateReviewDto review)
            throws BaseApplicationException {
        Review newReview = new Review();
        PhotographerInfo photographer = profileService.findPhotographerInfo(review.getPhotographerLogin());
        String user = authCtx.getCurrentUsersLogin();
        Account account = accountService.findByLogin(user);
        newReview.setPhotographer(photographer);
        newReview.setAccount(account);
        newReview.setActive(true);
        newReview.setScore(review.getScore());
        newReview.setContent(review.getContent());
        newReview.setLikeCount(0L);
        reviewService.addPhotographerReview(newReview);
        profileService.updateScore(photographer, review.getScore(), 1L);
    }

    /**
     * Usuwa recenzje dodana przez zalogowanego użytkownika
     *
     * @param reviewId id recenzji fotografa
     *
     * @throws BaseApplicationException w przypadku niepowodzenia operacji
     *
     */
    @RolesAllowed(deleteOwnPhotographerReview)
    public void deleteOwnPhotographerReview(Long reviewId)
            throws BaseApplicationException {
        Review review = reviewService.findById(reviewId);
        String user = authCtx.getCurrentUsersLogin();
        Account account = accountService.findByLogin(user);
        if (!review.getAccount().equals(account)) {
            throw ExceptionFactory.noAuthenticatedAccountFound();
        }
        PhotographerInfo photographer = review.getPhotographer();
        reviewService.deletePhotographerReview(review);
        profileService.updateScore(photographer, -review.getScore(), -1L);
    }

    /**
     * Usuwa recenzje dodana przez dowolnego użytkownika
     *
     * @param reviewId id recenzji fotografa
     *
     * @throws BaseApplicationException w przypadku niepowodzenia operacji
     *
     */
    @RolesAllowed(deleteSomeonesPhotographerReview)
    public void deleteSomeonesPhotographerReview(Long reviewId)
            throws BaseApplicationException {
        Review review = reviewService.findById(reviewId);
        PhotographerInfo photographer = review.getPhotographer();
        reviewService.deletePhotographerReview(review);
        profileService.updateScore(photographer, -review.getScore(), -1L);
    }

    /**
     * Wykonuje polubienie danej recenzji przez zalogowanego użytkownika
     * @param reviewId  id recenzji mającej być polubioną
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
}
