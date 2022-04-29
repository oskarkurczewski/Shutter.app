package pl.lodz.p.it.ssbd2022.ssbd02.mok.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.User;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.WrongNewPasswordException;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.UserUpdatePasswordDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.facade.AuthenticationFacade;

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

    @RolesAllowed({"ADMINISTRATOR"})
    public void changeUserPasswordAsAdmin(UserUpdatePasswordDto data) {
        User target = userFacade.find(data.getId());
        String newPassword = data.getPassword();
        if (newPassword.trim().length() < 9) {
            throw new WrongNewPasswordException("New password cannot be applied");
        }
        String hashed = BCrypt.withDefaults().hashToString(6, newPassword.toCharArray());
        target.setPassword(hashed);
        userFacade.update(target);
    }
}
