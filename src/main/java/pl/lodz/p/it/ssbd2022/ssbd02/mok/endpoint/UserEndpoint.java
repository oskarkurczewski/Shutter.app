package pl.lodz.p.it.ssbd2022.ssbd02.mok.endpoint;

import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.DataNotFoundException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAuthenticatedUserFound;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoUserFound;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.EditUserInfoDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.UserUpdatePasswordDto;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.User;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.UserRegisterDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.UserInfoDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.service.UserService;

import javax.annotation.security.PermitAll;
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
    public void blockUser(String login, Boolean active) throws NoUserFound {
        userService.changeAccountStatus(login, active);
    }


    /**
     * Wywołuję funkcję do edycji danych użytkownika
     *
     * @param editUserInfoDto klasa zawierająca zmienione dane danego użytkownika
     * @throws NoAuthenticatedUserFound
     */
    @RolesAllowed({"ADMINISTRATOR", "MODERATOR", "PHOTOGRAPHER", "CLIENT"})
    public void editUserInfo(EditUserInfoDto editUserInfoDto) throws NoAuthenticatedUserFound {
        // Można zwrócić użytkownika do userController w przyszłości, trzeba tylko opakowac go w dto
        userService.editUserInfo(editUserInfoDto);
    }

    /**
     * Konwertuje obiekt transferu danych użytkownika na obiekt klasy encji
     *
     * @param userRegisterDto Obiekt zawierający dane użytkownika
     * @throws BaseApplicationException Występuje w przypadku gdy rejestracja się nie powiedzie
     */
    @PermitAll
    public void registerUser(UserRegisterDto userRegisterDto) throws BaseApplicationException {

        User user = new User();
        user.setLogin(userRegisterDto.getLogin());
        user.setPassword(userRegisterDto.getPassword());
        user.setEmail(userRegisterDto.getEmail());
        user.setName(userRegisterDto.getName());
        user.setSurname(userRegisterDto.getSurname());
        userService.registerUser(user);
    }

    @RolesAllowed({"ADMINISTRATOR"})
    public void updatePasswordAsAdmin(Long id, UserUpdatePasswordDto password) {
        userService.changeUserPasswordAsAdmin(id, password);
    }
    
    @RolesAllowed({"ADMINISTRATOR", "MODERATOR", "USER", "PHOTOGRAPHER"})
    public UserInfoDto getUserInfo(String login) throws DataNotFoundException {
        return userService.getUserInfo(login);
    }
    
    
}
