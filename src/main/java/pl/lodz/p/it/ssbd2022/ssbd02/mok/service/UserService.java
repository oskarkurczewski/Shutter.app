package pl.lodz.p.it.ssbd2022.ssbd02.mok.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.User;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.PasswordMismatchException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.WrongPasswordException;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.UserUpdatePasswordDto;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAuthenticatedUser;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.EditUserInfoDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.facade.AuthenticationFacade;
import pl.lodz.p.it.ssbd2022.ssbd02.security.AuthenticationContext;

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

    @Inject
    private AuthenticationContext authenticationContext;

    @RolesAllowed({"ADMINISTRATOR", "MODERATOR"})
    public void changeAccountStatus(String login, Boolean active) throws NoAuthenticatedUser {
        User user = userFacade.findByLogin(login);
        user.setActive(active);
        userFacade.getEm().merge(user); // TODO Po implementacji transakcyjności zmineić na wywołanie metody update fasady
    }

    /**
     * Metoda pozwalająca administratorowi zmienić hasło dowolnego użytkowika
     * @param userId ID użytkownika, którego hasło administrator chce zmienić
     * @param data obiekt zawierający nowe hasło dla wskazanego użytkownika
     */
    @RolesAllowed({"ADMINISTRATOR"})
    public void changeUserPasswordAsAdmin(Long userId, UserUpdatePasswordDto data) {
        User target = userFacade.find(userId);
        changePassword(target, data.getPassword());
    }

    /**
     * Metoda pozwalająca zmienić własne hasło
     * @param data obiekt zawierający stare hasło (w celu werfyikacji) oraz nowe mające być ustawione dla użytkownika
     */
    @RolesAllowed({"ADMINISTRATOR", "MODERATOR", "PHOTOGRAPHER", "CLIENT"})
    public void updateOwnPassword(UserUpdatePasswordDto data) throws NoAuthenticatedUser {
        if (data.getOldPassword() == null) {
            throw new WrongPasswordException("Old password cannot be null");
        }
        User current = authenticationContext.getCurrentUser();
        String oldHash = BCrypt.withDefaults().hashToString(6, data.getOldPassword().toCharArray());
        if (!oldHash.equals(current.getPassword())) {
            throw new PasswordMismatchException();
        }
        changePassword(current, data.getPassword());
    }

    /**
     * Pomocnicza metoda utworzone w celu uniknięcia powtarzania kodu.
     * Zmienia hasło wskazanego użytkownika
     * @param target ID użytkownika, którego modyfikujemy
     * @param newPassword nowe hasło dla użytkownika
     */
    private void changePassword(User target, String newPassword) {
        if (newPassword.trim().length() < 8) {
            throw new WrongPasswordException("New password cannot be applied");
        }
        String hashed = BCrypt.withDefaults().hashToString(6, newPassword.toCharArray());
        target.setPassword(hashed);
        userFacade.update(target);
    }

    @RolesAllowed({"ADMINISTRATOR", "MODERATOR", "PHOTOGRAPHER", "CLIENT"})
    public User editUserInfo(EditUserInfoDto editUserInfoDto) throws NoAuthenticatedUser {
        User user = null;
        user = authenticationContext.getCurrentUser();
        user.setEmail(editUserInfoDto.getEmail());
        user.setName(editUserInfoDto.getName());
        user.setSurname(editUserInfoDto.getSurname());
        return userFacade.update(user);
    }
}
