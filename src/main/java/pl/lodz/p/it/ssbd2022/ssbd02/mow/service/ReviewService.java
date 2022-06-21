package pl.lodz.p.it.ssbd2022.ssbd02.mow.service;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Review;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.ExceptionFactory;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.dto.ListResponseDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.dto.ReviewDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.facade.PhotographerInfoFacade;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.facade.ReviewFacade;
import pl.lodz.p.it.ssbd2022.ssbd02.security.AuthenticationContext;
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
import java.util.Optional;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.*;

@Stateless
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class ReviewService {

    @Inject
    private ReviewFacade reviewFacade;

    @Inject
    private PhotographerInfoFacade photographerInfoFacade;

    @Inject
    private AuthenticationContext authenticationContext;

    @PermitAll
    public Review findById(Long id) throws BaseApplicationException {
        return Optional.ofNullable(reviewFacade.find(id)).orElseThrow(ExceptionFactory::noReviewFoundException);
    }

    @PermitAll
    public ListResponseDto<ReviewDto> listReviewsByPhotographerLogin(int pageNo, int recordsPerPage, String login) throws BaseApplicationException {
        Long photographerId = photographerInfoFacade.findPhotographerByLogin(login).getId();
        Long allRecords = reviewFacade.getReviewListSize(photographerId);
        List<Review> list = reviewFacade.getReviewListByPhotographer(pageNo, recordsPerPage, photographerId);
        List<ReviewDto> reviewDtoList = new ArrayList<>();
        String currentUserlogin = authenticationContext.getCurrentUsersLogin();

        for (Review review : list) {
            boolean liked = review.getLikedList().stream().anyMatch(p -> p.getLogin().equals(currentUserlogin));
            reviewDtoList.add(new ReviewDto(review, liked));
        }

        ListResponseDto<ReviewDto> listResponseDto = new ListResponseDto<>(
                pageNo,
                (int) Math.ceil(allRecords.doubleValue() / recordsPerPage),
                recordsPerPage,
                allRecords,
                reviewDtoList
        );

        return listResponseDto;
    }

    /**
     * Wykonuje operację usuniecia recenzji fotografa
     *
     * @param review recenzja, która ma być usunieta
     *
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
