package pl.lodz.p.it.ssbd2022.ssbd02.mok.service;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.User;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.AuthenticationFacade;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class UserService {

    @Inject
    private AuthenticationFacade userFacade;

    @RolesAllowed({"ADMINISTRATOR", "MODERATOR"})
    public void changeAccountStatus(String login, Boolean active) {
        User user = userFacade.findByLogin(login);
        user.setActive(active);
        userFacade.getEm().merge(user); // TODO Po implementacji transakcyjności zmineić na wywołanie metody update fasady
    }
}
