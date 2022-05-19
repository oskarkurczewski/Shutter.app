package pl.lodz.p.it.ssbd2022.ssbd02.mow.endpoint;

import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAuthenticatedAccountFound;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateful;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.*;

@Stateful
public class PhotographerEndpoint {

    @RolesAllowed(changePhotographerDescription)
    public void changeDescription(String newDescription) throws NoAuthenticatedAccountFound {
        throw new UnsupportedOperationException();
    }
}
