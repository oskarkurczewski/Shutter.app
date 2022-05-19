package pl.lodz.p.it.ssbd2022.ssbd02.mow.endpoint;

import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAuthenticatedAccountFound;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoPhotoFoundException;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.dto.AddPhotoDto;
import pl.lodz.p.it.ssbd2022.ssbd02.util.AbstractEndpoint;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateful;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.*;
import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.unlikePhoto;

@Stateful
public class PhotoEndpoint extends AbstractEndpoint {
    @RolesAllowed(addPhotoToGallery)
    public void addPhotoToGallery(AddPhotoDto addPhotoDto) throws NoAuthenticatedAccountFound {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(deletePhotoFromGallery)
    public void deletePhotoFromGallery(Long photoId) throws NoAuthenticatedAccountFound, NoPhotoFoundException {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(likePhoto)
    public void likePhoto(Long photoId) throws NoAuthenticatedAccountFound, NoPhotoFoundException {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(unlikePhoto)
    public void unlikePhoto(Long photoId) throws NoAuthenticatedAccountFound, NoPhotoFoundException {
        throw new UnsupportedOperationException();
    }
}
