package pl.lodz.p.it.ssbd2022.ssbd02.mok.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.User;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.facade.AuthenticationFacade;
import pl.lodz.p.it.ssbd2022.ssbd02.security.PasswordHashImpl;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class UserService {

    @Inject
    private AuthenticationFacade userFacade;

    @RolesAllowed({"ADMINISTRATOR", "MODERATOR"})
    public void changeAccountStatus(String login, Boolean active) {
        User user = userFacade.findByLogin(login);
        user.setActive(active);
        userFacade.getEm().merge(user); // TODO Po implementacji transakcyjności zmineić na wywołanie metody update fasady
    }

    @PermitAll
    public void registerUser(User user) throws BaseApplicationException {

        user.setPassword(BCrypt.withDefaults().hashToString(6, user.getPassword().toCharArray()));
        user.setActive(false);
        user.setRegistered(false);
        userFacade.registerUser(user);
    }
}
