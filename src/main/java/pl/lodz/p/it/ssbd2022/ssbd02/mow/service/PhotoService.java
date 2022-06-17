package pl.lodz.p.it.ssbd2022.ssbd02.mow.service;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Photo;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.PhotographerInfo;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.ExceptionFactory;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.PhotoAlreadyLikedException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.PhotoAlreadyUnlikedException;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.facade.PhotoFacade;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.facade.ProfileFacade;
import pl.lodz.p.it.ssbd2022.ssbd02.util.LoggingInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd02.util.S3Service;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.util.UUID;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.*;

@Stateless
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class PhotoService {

    @Inject
    private S3Service s3Service;

    @Inject
    private PhotoFacade photoFacade;

    @Inject
    private ProfileFacade profileFacade;

    @PermitAll
    public Photo findById(Long id) throws BaseApplicationException {
        return photoFacade.find(id);
    }

    /**
     * Dodaje nowe zdjęcie do galerii fotografa o podanym loginie oraz umieszcza ów zdjęcie w serwisie AWS S3
     *
     * @param login       login fotografa, w którego galerii ma zostać umieszczone zdjęcie
     * @param data        zdjęcie w postaci ciągu bajtów
     * @param title       tytuł zdjęcia
     * @param description opis zdjęcia
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @RolesAllowed(addPhotoToGallery)
    public void addPhoto(String login, byte[] data, String title, String description) throws BaseApplicationException {
        PhotographerInfo photographerInfo = profileFacade.findByLogin(login);
        String objectKey = UUID.randomUUID().toString().replace("-", "");
        Photo newPhoto = new Photo();
        newPhoto.setTitle(title);
        newPhoto.setDescription(description);
        newPhoto.setLikeCount(0L);
        newPhoto.setPhotographer(photographerInfo);

        String s3Url = s3Service.uploadObject(login, data, objectKey);
        newPhoto.setS3Url(s3Url);
        newPhoto.setObjectKey(objectKey);

        photoFacade.persist(newPhoto);
    }

    /**
     * Usuwa podane zdjęcie z galerii użytkownika
     *
     * @param photo zdjęcie które ma zostać usunięte z galerii
     */
    @RolesAllowed(deletePhotoFromGallery)
    public void deletePhoto(Photo photo) throws BaseApplicationException {
        photoFacade.remove(photo);
        s3Service.deleteObject(photo.getPhotographer().getAccount().getLogin(), photo.getObjectKey());
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

    /**
     * Usuwa polubienie użytkownika na zdjęciu o danym Id
     *
     * @param photo Wybrane zdjęcie
     * @param account Użytkownik
     * @throws PhotoAlreadyUnlikedException zdjęcie zostało już polubione przez danego użytkownika
     */
    @RolesAllowed(unlikePhoto)
    public void unlikePhoto(Photo photo, Account account) throws PhotoAlreadyUnlikedException {
        if (!photo.getLikesList().contains(account) || !account.getLikedPhotosList().contains(photo)) {
            throw ExceptionFactory.photoAlreadyUnlikedException();
        }

        photo.getLikesList().remove(account);
        account.getLikedPhotosList().remove(photo);
        photo.setLikeCount(photo.getLikeCount() - 1);
    }

}
