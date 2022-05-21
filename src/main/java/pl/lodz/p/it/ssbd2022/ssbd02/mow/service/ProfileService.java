package pl.lodz.p.it.ssbd2022.ssbd02.mow.service;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.PhotographerInfo;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.*;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.*;

@Stateless
public class ProfileService {

    @PermitAll
    public PhotographerInfo findPhotographerInfo(String login) {
        throw new UnsupportedOperationException();
    }

    @RolesAllowed(changePhotographerDescription)
    public void changeDescription(PhotographerInfo photographer, String newDescription) {
        throw new UnsupportedOperationException();
    }
}
