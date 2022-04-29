package pl.lodz.p.it.ssbd2022.ssbd02.mok.endpoint;

import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAuthenticatedUser;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.service.UserService;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class UserEndpoint {

    @Inject
    private UserService userService;

    @RolesAllowed({"ADMINISTRATOR", "MODERATOR"})
    public void blockUser(String login, Boolean active) throws NoAuthenticatedUser {
        userService.changeAccountStatus(login, active);
    }
}
