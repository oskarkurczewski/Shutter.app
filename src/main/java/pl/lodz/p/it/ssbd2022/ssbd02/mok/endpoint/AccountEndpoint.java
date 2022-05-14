package pl.lodz.p.it.ssbd2022.ssbd02.mok.endpoint;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.DatabaseException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.IdenticalFieldException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAccountFound;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAuthenticatedAccountFound;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.*;
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
     * Ustawia status użytkownika o danym loginie na podany
     *
     * @param login  login użytkownika, dla którego chcemy zmienić status
     * @param active status, który chcemy ustawić dla konta tego użytkownika
     * @throws NoAccountFound, kiedy użytkownik o danym loginie nie zostanie odnaleziony
     *                         w bazie danych
     */
    @RolesAllowed({blockAccount, unblockAccount})
    public void changeAccountStatus(String login, Boolean active) throws NoAccountFound {
        accountService.changeAccountStatus(login, active);
    }

    /**
     * Konwertuje obiekt transferu danych użytkownika na obiekt klasy encji.
     *
     * @param accountRegisterDto Obiekt zawierający dane użytkownika
     * @throws IdenticalFieldException, Występuje w przypadku gdy rejestracja się nie powiedzie
     * @throws DatabaseException,       Występuje w przypadku gdy rejestracja się nie powiedzie
     */
    @PermitAll
    public void registerAccount(AccountRegisterDto accountRegisterDto) throws IdenticalFieldException, DatabaseException {
        Account account = accountRegisterDtoToAccount(accountRegisterDto);
        accountService.registerAccount(account);
    }

    /**
     * Konwertuje obiekt transferu danych użytkownika (z dodatkowymi polami registered oraz active) obiekt klasy encji.
     *
     * @param accountRegisterAsAdminDto Obiekt zawierający dane użytkownika (z dodatkowymi polami registered oraz active)
     * @throws IdenticalFieldException, Występuje w przypadku gdy rejestracja się nie powiedzie
     * @throws DatabaseException,       Występuje w przypadku gdy rejestracja się nie powiedzie
     */
    @RolesAllowed({"ADMINISTRATOR"})
    public void registerAccountByAdmin(AccountRegisterAsAdminDto accountRegisterAsAdminDto) throws IdenticalFieldException, DatabaseException {
        Account account = accountRegisterDtoToAccount(accountRegisterAsAdminDto);
        account.setActive(accountRegisterAsAdminDto.getActive());
        account.setRegistered(accountRegisterAsAdminDto.getRegistered());
        accountService.registerAccountByAdmin(account);
    }

    @RolesAllowed({"ADMINISTRATOR"})
    public void updatePasswordAsAdmin(Long id, AccountUpdatePasswordDto password) {
        accountService.changeAccountPasswordAsAdmin(id, password);
    }

    /**
     * Metoda pomocnicza konwertująca obiekt transferu danych na obiekt encji użytkownika
     *
     * @param accountRegisterDto Obiekt zawierający dane użytkownika
     * @return Obiekt klasy encji użytkownika
     */
    private Account accountRegisterDtoToAccount(AccountRegisterDto accountRegisterDto) throws IdenticalFieldException, DatabaseException {
        Account account = new Account();
        account.setLogin(accountRegisterDto.getLogin());
        account.setPassword(accountRegisterDto.getPassword());
        account.setEmail(accountRegisterDto.getEmail());
        account.setName(accountRegisterDto.getName());
        account.setSurname(accountRegisterDto.getSurname());
        accountService.registerAccount(account);
        return account;
    }

    /**
     * Wywołuję funkcję do edycji danych użytkownika
     *
     * @param editAccountInfoDto klasa zawierająca zmienione dane danego użytkownika
     * @throws NoAuthenticatedAccountFound W przypadku gdy nie znaleziono aktualnego użytkownika
     */
    @RolesAllowed(editOwnAccountData)
    public void editAccountInfo(EditAccountInfoDto editAccountInfoDto) throws NoAuthenticatedAccountFound {
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
        // Można zwrócić użytkownika do userController w przyszłości, trzeba tylko opakować go w dto
        accountService.editAccountInfoAsAdmin(login, editAccountInfoDto);
    }

    /**
     * Szuka użytkownika
     *
     * @param login nazwa użytkownika
     * @return obiekt DTO informacji o użytkowniku
     * @throws NoAccountFound              W przypadku gdy użytkownik o podanej nazwie nie istnieje lub
     *                                     gdy konto szukanego użytkownika jest nieaktywne, lub niepotwierdzone i informacje próbuje uzyskać użytkownik
     *                                     niebędący ani administratorem, ani moderatorem
     * @throws NoAuthenticatedAccountFound W przypadku gdy dane próbuje uzyskać niezalogowana osoba
     * @see AccountInfoDto
     */
    @RolesAllowed({"ADMINISTRATOR", "MODERATOR", "USER", "PHOTOGRAPHER"})
    public AccountInfoDto getAccountInfo(String login) throws NoAuthenticatedAccountFound, NoAccountFound {
        return accountService.getAccountInfo(login);
    }

    /**
     * Zwraca informacje o zalogowanym użytkowniku
     *
     * @return obiekt DTO informacji o użytkowniku
     * @throws NoAuthenticatedAccountFound W przypadku gdy dane próbuje uzyskać niezalogowana osoba
     * @see AccountInfoDto
     */
    @RolesAllowed(changeOwnPassword)
    public AccountInfoDto getYourAccountInfo() throws NoAuthenticatedAccountFound {
        return accountService.getYourAccountInfo();
    }


    @RolesAllowed({"ADMINISTRATOR", "MODERATOR", "PHOTOGRAPHER", "CLIENT"})
    public void updateOwnPassword(AccountUpdatePasswordDto data) throws NoAuthenticatedAccountFound {
        accountService.updateOwnPassword(data);
    }
}
