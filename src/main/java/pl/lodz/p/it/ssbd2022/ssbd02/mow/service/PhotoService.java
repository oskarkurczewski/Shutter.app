package pl.lodz.p.it.ssbd2022.ssbd02.mow.service;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Photo;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.*;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.facade.PhotoFacade;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.*;
import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.unlikePhoto;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class PhotoService {

    @Inject
    private PhotoFacade photoFacade;

    @PermitAll
    public Photo findById(Long id) throws BaseApplicationException {
        return photoFacade.find(id);
    }

    @RolesAllowed(addPhotoToGallery)
    public void addPhoto(Photo photo) {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(deletePhotoFromGallery)
    public void deletePhoto(Photo photo) {
        throw new UnsupportedOperationException();
    }

    /**
     * Metoda dodająca polubienie zdjęcia przez danego użytkownika
     *
     * @param photo   polubione zdjęcie
     * @param account użytkownik dokonujący polubienie
     * @throws PhotoAlreadyLikedException w przypadku gry zdjęcie zostało już polubione
     */

    @RolesAllowed(likePhoto)
    public void likePhoto(Photo photo, Account account) throws BaseApplicationException {
        if (photo.getLikesList().contains(account) || account.getLikedPhotosList().contains(photo)) {
            throw ExceptionFactory.photoAlreadyLikedException();
        }
        photo.getLikesList().add(account);
        account.getLikedPhotosList().add(photo);
        photo.setLikeCount(photo.getLikeCount() + 1);
    }

    @RolesAllowed(unlikePhoto)
    public void unlikePhoto(Photo photo) {
        throw new UnsupportedOperationException();
    }

}
