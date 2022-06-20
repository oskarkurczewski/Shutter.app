package pl.lodz.p.it.ssbd2022.ssbd02.mow.service;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Review;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.ExceptionFactory;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.facade.ReviewFacade;
import pl.lodz.p.it.ssbd2022.ssbd02.util.LoggingInterceptor;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

import java.util.List;
import java.util.Optional;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.*;

@Stateless
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class ReviewService {

    @Inject
    private ReviewFacade reviewFacade;

    /**
     * Pozyskuje recenzję na podstawie jej identyfikatora
     *
     * @param id identyfikator recenzji
     * @return recenzja o danym identyfikatorze
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @PermitAll
    public Review findById(Long id) throws BaseApplicationException {
        return Optional.ofNullable(reviewFacade.find(id)).orElseThrow(ExceptionFactory::noReviewFoundException);
    }

    /**
     * Pozyskuje listę recenzji fotografa o danym identyfikatorze
     *
     * @param pageNo         numer strony
     * @param recordsPerPage liczba krotek na stronę
     * @param id             identyfikator fotografa
     * @return lista recenzji danego fotografa
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @PermitAll
    public List<Review> listReviewsByPhotographerId(int pageNo, int recordsPerPage, Long id) throws BaseApplicationException {
        return reviewFacade.getReviewListByPhotographer(pageNo, recordsPerPage, id);
    }

    /**
     * Wykonuje operację usuniecia recenzji fotografa
     *
     * @param review recenzja, która ma być usunieta
     * @throws BaseApplicationException Gdy operacja się nie powiedzie
     */
    @RolesAllowed({deleteOwnPhotographerReview, deleteSomeonesPhotographerReview})
    public void deletePhotographerReview(Review review) throws BaseApplicationException {
        if (review.getActive() == false) {
            throw ExceptionFactory.cannotChangeException();
        }
        review.setActive(false);
        reviewFacade.update(review);
    }

    /**
     * Wykonuje operację dodania recenzji fotografowi przez wskaznego użytkownika
     *
     * @param review recenzja, która ma być dodana
     * @throws BaseApplicationException Gdy operacja się nie powiedzie
     */
    @RolesAllowed(reviewPhotographer)
    public void addPhotographerReview(Review review) throws BaseApplicationException {
        reviewFacade.persist(review);
    }

    /**
     * Wykonuje operację polubienia recenzji przez wskaznego użytkownika
     *
     * @param account konto użytkownika wykonującego polubienie
     * @param review  recenzja mająca być polubiona
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

    /**
     * Anuluje polubienie recenzji danego fotografa
     *
     * @param account konto, które dokonuje anulowania polubienia
     * @param review  recenzja, dla którego polubienie jest anulowane
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @RolesAllowed(unlikeReview)
    public void unlikeReview(Account account, Review review) throws BaseApplicationException {
        if (review.getLikedList().stream().noneMatch(u -> u.getLogin().equals(account.getLogin()))) {
            throw ExceptionFactory.alreadyUnlikedException();
        }
        review.removeLikeFromUser(account);
        review.setLikeCount(review.getLikeCount() - 1);
        reviewFacade.update(review);
    }
}
