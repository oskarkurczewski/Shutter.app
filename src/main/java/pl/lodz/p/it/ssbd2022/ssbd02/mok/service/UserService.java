package pl.lodz.p.it.ssbd2022.ssbd02.mok.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.AccessLevelAssignment;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.AccessLevelValue;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.User;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.*;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.EditUserInfoDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.UserUpdatePasswordDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.UserInfoDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.facade.AuthenticationFacade;
import pl.lodz.p.it.ssbd2022.ssbd02.security.AuthenticationContext;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import java.util.List;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class UserService {

    @Inject
    private AuthenticationFacade userFacade;

    @Inject
    private AuthenticationContext authenticationContext;

    @RolesAllowed({"ADMINISTRATOR", "MODERATOR"})
    public void changeAccountStatus(String login, Boolean active) throws NoUserFound {
        User user = userFacade.findByLogin(login);
        user.setActive(active);
        userFacade.getEm().merge(user); // TODO Po implementacji transakcyjności zmineić na wywołanie metody update fasady
    }

    /**
     * Funckja do edycji użytkownika. Zmienia tylko proste informacje a nie role dostępu itp
     *
     * @param editUserInfoDto klasa zawierająca zmienione dane danego użytkownika
     * @return obiekt użytkownika po aktualizacji
     * @throws NoAuthenticatedUserFound W przypadku gdy nie znaleziono aktualnego użytkownika
     */
    @RolesAllowed({"ADMINISTRATOR", "MODERATOR", "PHOTOGRAPHER", "CLIENT"})
    public User editUserInfo(EditUserInfoDto editUserInfoDto) throws NoAuthenticatedUserFound {
        User user = null;
        user = authenticationContext.getCurrentUser();
        user.setEmail(editUserInfoDto.getEmail());
        user.setName(editUserInfoDto.getName());
        user.setSurname(editUserInfoDto.getSurname());
        return userFacade.update(user);
    }
    
    @RolesAllowed({"ADMINISTRATOR", "MODERATOR", "USER", "PHOTOGRAPHER"})
    public UserInfoDto getUserInfo(String login) throws DataNotFoundException {
        User user = null; 
        try {
            user = userFacade.findByLogin(login);
        } catch (NoUserFound e) {
            throw new DataNotFoundException("exception.account.notfound");
        }
        return new UserInfoDto(user);
    }
    
    

    @RolesAllowed({"ADMINISTRATOR"})
    public void changeUserPasswordAsAdmin(Long userId, UserUpdatePasswordDto data) {
        User target = userFacade.find(userId);
        String newPassword = data.getPassword();
        if (newPassword.trim().length() < 8) {
            throw new WrongNewPasswordException("New password cannot be applied");
        }
        String hashed = BCrypt.withDefaults().hashToString(6, newPassword.toCharArray());
        target.setPassword(hashed);
        userFacade.update(target);
    }

    /**
     * Rejestruje konto użytkownika z danych podanych w obiekcie klasy użytkownika
     * oraz przypisuje do niego poziom dostępu klienta.
     * W celu aktywowania konta należy jeszcze zmienić pole 'registered' na wartość 'true'
     *
     * @param user Obiekt klasy User reprezentującej dane użytkownika
     * @throws BaseApplicationException Wyjątek otrzymywany w przypadku niepowodzenia rejestracji (login lub adres email już istnieje)
     * @see User
     */
    @PermitAll
    public void registerUser(User user) throws BaseApplicationException {

        user.setPassword(BCrypt.withDefaults().hashToString(6, user.getPassword().toCharArray()));
        user.setActive(true);
        user.setRegistered(false);

        AccessLevelValue levelValue = userFacade.getAccessLevelValue("CLIENT");

        AccessLevelAssignment assignment = new AccessLevelAssignment();
        assignment.setLevel(levelValue);
        assignment.setUser(user);
        assignment.setActive(true);

        List<AccessLevelAssignment> list = user.getAccessLevelAssignmentList();
        list.add(assignment);

        user.setAccessLevelAssignmentList(list);

        userFacade.registerUser(user);
    }
}
