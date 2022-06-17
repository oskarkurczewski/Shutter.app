package pl.lodz.p.it.ssbd2022.ssbd02.mow.endpoint;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.PhotographerInfo;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Review;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAuthenticatedAccountFound;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoReviewFoundException;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.dto.CreateReviewDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.dto.ReviewDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.service.AccountService;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.dto.CreateReviewDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.service.ProfileService;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.service.PhotographerService;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.service.ReviewService;
import pl.lodz.p.it.ssbd2022.ssbd02.security.AuthenticationContext;
import pl.lodz.p.it.ssbd2022.ssbd02.util.AbstractEndpoint;
import pl.lodz.p.it.ssbd2022.ssbd02.util.LoggingInterceptor;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

import java.util.ArrayList;
import java.util.List;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.*;

@Stateless
@Interceptors({LoggingInterceptor.class})
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
    @Inject
    private PhotographerService photographerService;

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
        profileService.updateScore(photographer, review.getScore());
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

    /**
     * Wykonuje usunięcie polubienia danej recenzji przez zalogowanego użytkownika
     *
     * @param reviewId id recenzji mającej przestać być polubioną
     * @throws BaseApplicationException
     */
    @RolesAllowed(unlikeReview)
    public void unlikeReview(Long reviewId) throws BaseApplicationException {
        Review review = reviewService.findById(reviewId);
        String user = authCtx.getCurrentUsersLogin();
        Account account = accountService.findByLogin(user);
        reviewService.unlikeReview(account, review);
    }

    @PermitAll
    public List<ReviewDto> getReviewsByPhotographerLogin(int pageNo, int recordsPerPage, String photographerLogin)
            throws BaseApplicationException {
        Long photographerId = photographerService.findByLogin(photographerLogin).getId();
        List<Review> reviews = reviewService.listReviewsByPhotographerId(pageNo, recordsPerPage, photographerId);
        List<ReviewDto> reviewDtoList = new ArrayList<>();

        String login = authCtx.getCurrentUsersLogin();

        for (Review review: reviews) {
            boolean liked = review.getLikedList().stream().anyMatch(r -> r.getLogin().equals(login));
            reviewDtoList.add(new ReviewDto(review, liked));
        }

        return reviewDtoList;
    }
}
