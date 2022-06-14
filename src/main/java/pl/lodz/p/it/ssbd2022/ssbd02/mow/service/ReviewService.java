package pl.lodz.p.it.ssbd2022.ssbd02.mow.service;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.PhotographerInfo;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Review;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.ExceptionFactory;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.facade.ReviewFacade;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.facade.ProfileFacade;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceException;

import java.util.Optional;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.*;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class ReviewService {

    @Inject
    private ReviewFacade reviewFacade;

    @Inject ProfileFacade profileFacade;

    @PermitAll
    public Review findById(Long id) throws BaseApplicationException {
        return Optional.ofNullable(reviewFacade.find(id)).orElseThrow(ExceptionFactory::noReviewFoundException);
    }

    @RolesAllowed({deleteOwnPhotographerReview, deleteSomeonesPhotographerReview})
    public void deletePhotographerReview(Review review) {
        throw new UnsupportedOperationException();
    }

    /**
     * Wykonuje operację dodania recenzji wskazanemu fotografowi przez wskaznego użytkownika
     *
     * @param review recenzja, która ma być dodana
     * @param photographer fotograf, któremu doawana jest recenzja
     *
     * @throws BaseApplicationException Gdy operacja się nie powiedzie
     */
    @RolesAllowed(reviewPhotographer)
    public void addPhotographerReview(Review review, PhotographerInfo photographer) throws BaseApplicationException {
        reviewFacade.persist(review);
        photographer.setScore(photographer.getScore() + review.getScore());
        photographer.setReviewCount(photographer.getReviewCount() + 1);
        profileFacade.update(photographer);
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
