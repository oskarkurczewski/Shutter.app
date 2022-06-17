package pl.lodz.p.it.ssbd2022.ssbd02.mow.endpoint;

import org.apache.commons.codec.binary.Base64;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Photo;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.dto.AddPhotoDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.service.AccountService;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.service.PhotoService;
import pl.lodz.p.it.ssbd2022.ssbd02.security.AuthenticationContext;
import pl.lodz.p.it.ssbd2022.ssbd02.util.AbstractEndpoint;
import pl.lodz.p.it.ssbd2022.ssbd02.util.LoggingInterceptor;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.*;

@Stateful
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class PhotoEndpoint extends AbstractEndpoint {

    @Inject
    private PhotoService photoService;

    @Inject
    private AuthenticationContext authenticationContext;

    @Inject
    private AccountService accountService;

    /**
     * Dodaje nowe zdjęcie do galerii obecnie uwierzytelnionego fotografa
     *
     * @param addPhotoDto obiekt DTO zawierający informacje potrzebne do dodania zdjęcia
     * @throws BaseApplicationException niepowodzenie operacji
     */
    @RolesAllowed(addPhotoToGallery)
    public void addPhotoToGallery(AddPhotoDto addPhotoDto) throws BaseApplicationException {
        String login = authenticationContext.getCurrentUsersLogin();
        photoService.addPhoto(
                login,
                Base64.decodeBase64(addPhotoDto.getData()),
                addPhotoDto.getTitle(),
                addPhotoDto.getDescription()
        );
    }

    /**
     * Usuwa zdjęcie o podanym identyfikatorze z galerii użytkownika
     *
     * @param photoId identyfikator zdjęcia fotografa, które ma zostać usunięte
     */
    @RolesAllowed(deletePhotoFromGallery)
    public void deletePhotoFromGallery(Long photoId) throws BaseApplicationException {
        Photo photo = photoService.findById(photoId);
        photoService.deletePhoto(photo);
    }

    @RolesAllowed(likePhoto)
    public void likePhoto(Long photoId) throws BaseApplicationException {
        Photo photo = photoService.findById(photoId);
        String login = authenticationContext.getCurrentUsersLogin();
        Account account = accountService.findByLogin(login);
        photoService.likePhoto(photo, account);
    }

    /**
     * Usuwa polubienie zdjęcia na wybranym zdjęciu
     *
     * @param photoId Id zdjęcia
     * @throws BaseApplicationException W przypadku niepowodzenia
     */
    @RolesAllowed(unlikePhoto)
    public void unlikePhoto(Long photoId) throws BaseApplicationException {
        Photo photo = photoService.findById(photoId);
        Account account = accountService.findByLogin(authenticationContext.getCurrentUsersLogin());
        photoService.unlikePhoto(photo, account);
    }
}
