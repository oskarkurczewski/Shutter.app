package pl.lodz.p.it.ssbd2022.ssbd02.mok.endpoint;

import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.UserRegisterDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.service.UserService;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class UserEndpoint {

    @Inject
    private UserService userService;

    @RolesAllowed({"ADMINISTRATOR", "MODERATOR"})
    public void blockUser(String login, Boolean active) {
        userService.changeAccountStatus(login, active);
    }

    @PermitAll
    public void registerUser(@NotNull @Valid UserRegisterDto userRegisterDto) {

//        User user = new User();
//        user.setLogin(userRegisterDto.getLogin());
//        user.setPassword(userRegisterDto.getPassword());
//        user.setEmail(userRegisterDto.getEmail());
//        user.setName(userRegisterDto.getName());
//        user.setSurname(userRegisterDto.getSurname());
//
//        userService.registerUser(user);
    }
}
