package pl.lodz.p.it.ssbd2022.ssbd02.mow.endpoint;

import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAuthenticatedAccountFound;
import pl.lodz.p.it.ssbd2022.ssbd02.util.AbstractEndpoint;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateful;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.*;

@Stateful
public class ProfileEndpoint extends AbstractEndpoint {

    @RolesAllowed(changePhotographerDescription)
    public void changeDescription(String newDescription) throws NoAuthenticatedAccountFound {
        throw new UnsupportedOperationException();
    }
}
