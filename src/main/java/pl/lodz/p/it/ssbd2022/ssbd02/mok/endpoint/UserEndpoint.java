package pl.lodz.p.it.ssbd2022.ssbd02.mok.endpoint;

import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.UserUpdatePasswordDto;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAuthenticatedUser;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.User;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.UserRegisterDto;
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

    /**
     * Ustawia status użytkownika o danym loginie na podany
     *
     * @param login login użytkownika dla którego chcemy zmienić status
     * @param active status który chcemy chcemy ustawić dla konta tego użytkownika
     * @throws NoAuthenticatedUser kiedy użytkonwik o danym loginie nie zostanie odnaleziony
     * w bazie danych
     */
    @RolesAllowed({"ADMINISTRATOR", "MODERATOR"})
    public void changeAccountStatus(String login, Boolean active) throws NoAuthenticatedUser {
        userService.changeAccountStatus(login, active);
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
}
