package pl.lodz.p.it.ssbd2022.ssbd02.mow.service;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Review;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.ExceptionFactory;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.facade.ReviewFacade;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import java.util.List;
import java.util.Optional;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.*;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class ReviewService {

    @Inject
    private ReviewFacade reviewFacade;

    @PermitAll
    public Review findById(Long id) throws BaseApplicationException {
        return Optional.ofNullable(reviewFacade.find(id)).orElseThrow(ExceptionFactory::noReviewFoundException);
    }

    @PermitAll
    public List<Review> listReviewsByPhotographerId(int pageNo, int recordsPerPage, Long id) throws BaseApplicationException {
        return reviewFacade.getReviewListByPhotographer(pageNo, recordsPerPage, id);
    }

    @RolesAllowed({deleteOwnPhotographerReview, deleteSomeonesPhotographerReview})
    public void deletePhotographerReview(Review review) {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(reviewPhotographer)
    public void addPhotographerReview(Review review) {
        throw new UnsupportedOperationException();
    }

    /**
     * Wykonuje operację polubienia recenzji przez wskaznego użytkownika
     * @param account konto użytkownika wykonującego polubienie
     * @param review recenzja mająca być polubiona
     * @throws BaseApplicationException Gdy operacja się nie powiedzie
     */
    @RolesAllowed(likeReview)
    public void likeReview(Account account, Review review) throws BaseApplicationException {
        if (review.getLikedList().stream().anyMatch(u -> u.getLogin().equals(account.getLogin()))) {
            throw ExceptionFactory.alreadyLikedException();
        }
        review.addLikeFromUser(account);
        review.setLikeCount(review.getLikeCount() + 1);
        reviewFacade.update(review);
    }

    @RolesAllowed(unlikeReview)
    public void unlikeReview(Account account, Review review) {
        throw new UnsupportedOperationException();
    }
}
