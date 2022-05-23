package pl.lodz.p.it.ssbd2022.ssbd02.mow.service;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Photo;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAuthenticatedAccountFound;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoPhotoFoundException;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.*;
import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.unlikePhoto;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class PhotoService {
    @PermitAll
    public Photo findById(Long id) throws NoPhotoFoundException {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(addPhotoToGallery)
    public void addPhoto(Photo photo) {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(deletePhotoFromGallery)
    public void deletePhoto(Photo photo) {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(likePhoto)
    public void likePhoto(Photo photo) {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(unlikePhoto)
    public void unlikePhoto(Photo photo) {
        throw new UnsupportedOperationException();
    }

}
