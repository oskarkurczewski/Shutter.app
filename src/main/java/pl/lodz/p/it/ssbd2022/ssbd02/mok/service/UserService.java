package pl.lodz.p.it.ssbd2022.ssbd02.mok.service;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.User;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAuthenticatedUserFound;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoUserFound;
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
}
