package pl.lodz.p.it.ssbd2022.ssbd02.mok.endpoint;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.DataNotFoundException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAuthenticatedUserFound;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAccountFound;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.AccountRegisterAsAdminDto;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.UnauthenticatedException;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.AccountInfoDto;
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
     * Ustawia status użytkownika o danym loginie na podany
     *
     * @param login login użytkownika dla którego chcemy zmienić status
     * @param active status który chcemy chcemy ustawić dla konta tego użytkownika
     * @throws NoAccountFound kiedy użytkonwik o danym loginie nie zostanie odnaleziony
     *                        w bazie danych
     */
    @RolesAllowed({blockAccount, unblockAccount})
    public void changeAccountStatus(String login, Boolean active) throws NoAccountFound {
        accountService.changeAccountStatus(login, active);
    }

    /**
     * Konwertuje obiekt transferu danych użytkownika na obiekt klasy encji.
     *
     * @param accountRegisterDto Obiekt zawierający dane użytkownika
     * @throws BaseApplicationException Występuje w przypadku gdy rejestracja się nie powiedzie
     */
    @PermitAll
    public void registerAccount(AccountRegisterDto accountRegisterDto) throws BaseApplicationException {
        Account account = accountRegisterDtoToAccount(accountRegisterDto);
        accountService.registerAccount(account);
    }

    /**
     * Konwertuje obiekt transferu danych użytkownika (z dodatkowymi polami registered oraz active) obiekt klasy encji.
     *
     * @param accountRegisterAsAdminDto Obiekt zawierający dane użytkownika (z dodatkowymi polami registered oraz active)
     * @throws BaseApplicationException Występuje w przypadku gdy rejestracja się nie powiedzie
     */
    @RolesAllowed({"ADMINISTRATOR"})
    public void registerAccountByAdmin(AccountRegisterAsAdminDto accountRegisterAsAdminDto) throws BaseApplicationException {
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
    private Account accountRegisterDtoToAccount(AccountRegisterDto accountRegisterDto) throws BaseApplicationException {
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

    /**
     * Szuka użytkownika
     *
     * @param login nazwa użytkownika
     * @return obiekt DTO informacji o użytkowniku
     * @throws DataNotFoundException W przypadku gdy użytkownik o podanej nazwie nie istnieje lub
     * gdy konto szukanego użytkownika jest nieaktywne lub niepotwierdzone i informacje prubuje uzyskać uzytkownik
     * niebędący ani administratorem ani moderatorem
     * @throws UnauthenticatedException W przypadku gdy dane próbuje uzyskać niezalogowana osoba
     * @see AccountInfoDto
     */
    @RolesAllowed({"ADMINISTRATOR", "MODERATOR", "USER", "PHOTOGRAPHER"})
    public AccountInfoDto getAccountInfo(String login) throws DataNotFoundException, UnauthenticatedException {
        return accountService.getAccountInfo(login);
    }

    /**
     * Zwraca informacje o zalogowanym użytkowniku
     *
     * @return obiekt DTO informacji o użytkowniku
     * @throws UnauthenticatedException W przypadku gdy dane próbuje uzyskać niezalogowana osoba
     * @see AccountInfoDto
     */
    @RolesAllowed(changeOwnPassword)
    public AccountInfoDto getYourAccountInfo() throws UnauthenticatedException {
        return accountService.getYourAccountInfo();
    }



    @RolesAllowed({"ADMINISTRATOR", "MODERATOR", "PHOTOGRAPHER", "CLIENT"})
    public void updateOwnPassword(AccountUpdatePasswordDto data) throws NoAuthenticatedUserFound {
        accountService.updateOwnPassword(data);
    }
}
