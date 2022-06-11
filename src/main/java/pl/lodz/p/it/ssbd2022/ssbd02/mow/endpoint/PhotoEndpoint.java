package pl.lodz.p.it.ssbd2022.ssbd02.mow.endpoint;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Photo;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAuthenticatedAccountFound;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoPhotoFoundException;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.dto.AddPhotoDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.service.AccountService;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.service.PhotoService;
import pl.lodz.p.it.ssbd2022.ssbd02.security.AuthenticationContext;
import pl.lodz.p.it.ssbd2022.ssbd02.util.AbstractEndpoint;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.*;
import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.unlikePhoto;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class PhotoEndpoint extends AbstractEndpoint {

    @Inject
    private PhotoService photoService;

    @Inject
    private AccountService accountService;

    @Inject
    private AuthenticationContext authenticationContext;

    @RolesAllowed(addPhotoToGallery)
    public void addPhotoToGallery(AddPhotoDto addPhotoDto) throws NoAuthenticatedAccountFound {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(deletePhotoFromGallery)
    public void deletePhotoFromGallery(Long photoId) throws NoAuthenticatedAccountFound, NoPhotoFoundException {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(likePhoto)
    public void likePhoto(Long photoId) throws BaseApplicationException {
        Photo photo = photoService.findById(photoId);
        String login = authenticationContext.getCurrentUsersLogin();
        Account account = accountService.findByLogin(login);
        photoService.likePhoto(photo, account);
    }

    @RolesAllowed(unlikePhoto)
    public void unlikePhoto(Long photoId) throws NoAuthenticatedAccountFound, NoPhotoFoundException {
        throw new UnsupportedOperationException();
    }
}
