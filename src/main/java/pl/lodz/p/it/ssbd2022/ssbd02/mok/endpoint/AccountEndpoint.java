package pl.lodz.p.it.ssbd2022.ssbd02.mok.endpoint;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAuthenticatedUserFound;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAccountFound;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.AccountUpdatePasswordDto;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.AccountRegisterDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.EditAccountInfoDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.service.AccountService;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.*;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class AccountEndpoint {

    @Inject
    private AccountService accountService;

    /**
     * Ustawia status użytkownika o danym loginie na zablokowany
     *
     * @param login  login użytkownika dla którego chcemy zmienić status
     * @throws NoAccountFound kiedy użytkonwik o danym loginie nie zostanie odnaleziony
     *                        w bazie danych
     */
    @RolesAllowed({blockAccount})
    public void blockAccount(String login) throws NoAccountFound {
        accountService.changeAccountStatus(login, false);
    }

    /**
     * Ustawia status użytkownika o danym loginie na odblokowany
     *
     * @param login  login użytkownika dla którego chcemy zmienić status
     * @throws NoAccountFound kiedy użytkonwik o danym loginie nie zostanie odnaleziony
     *                        w bazie danych
     */
    @RolesAllowed({unblockAccount})
    public void unblockAccount(String login) throws NoAccountFound {
        accountService.changeAccountStatus(login, true);
    }

    /**
     * Konwertuje obiekt transferu danych użytkownika na obiekt klasy encji
     *
     * @param accountRegisterDto Obiekt zawierający dane użytkownika
     * @throws BaseApplicationException Występuje w przypadku gdy rejestracja się nie powiedzie
     */
    @PermitAll
    public void registerAccount(AccountRegisterDto accountRegisterDto) throws BaseApplicationException {

        Account account = new Account();
        account.setLogin(accountRegisterDto.getLogin());
        account.setPassword(accountRegisterDto.getPassword());
        account.setEmail(accountRegisterDto.getEmail());
        account.setName(accountRegisterDto.getName());
        account.setSurname(accountRegisterDto.getSurname());
        accountService.registerAccount(account);
    }

    /**
     * Wywołuję funkcję do edycji danych użytkownika
     *
     * @param editAccountInfoDto klasa zawierająca zmienione dane danego użytkownika
     * @throws NoAuthenticatedUserFound W przypadku gdy nie znaleziono aktualnego użytkownika
     */
    @RolesAllowed(editOwnAccountData)
    public void editAccountInfo(EditAccountInfoDto editAccountInfoDto) throws NoAuthenticatedUserFound {
        // Można zwrócić użytkownika do userController w przyszłości, trzeba tylko opakowac go w dto
        accountService.editAccountInfo(editAccountInfoDto);
    }

    /**
     * Wywołuję funkcję do edycji danych użytkownika przez administratora
     *
     * @param editAccountInfoDto klasa zawierająca zmienione dane danego użytkownika
     * @throws NoAccountFound W przypadku gdy nie znaleziono użytkownika o danym loginie
     */
    @RolesAllowed({"ADMINISTRATOR"})
    public void editAccountInfoAsAdmin(String login, EditAccountInfoDto editAccountInfoDto) throws NoAccountFound {
        // Można zwrócić użytkownika do userController w przyszłości, trzeba tylko opakowac go w dto
        accountService.editAccountInfoAsAdmin(login, editAccountInfoDto);
    }

    @RolesAllowed({"ADMINISTRATOR"})
    public void updatePasswordAsAdmin(Long id, AccountUpdatePasswordDto password) {
        accountService.changeAccountPasswordAsAdmin(id, password);
    }

    @RolesAllowed(changeOwnPassword)
    public void updateOwnPassword(AccountUpdatePasswordDto data) throws NoAuthenticatedUserFound {
        accountService.updateOwnPassword(data);
    }
}
