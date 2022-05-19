package pl.lodz.p.it.ssbd2022.ssbd02.mok.endpoint;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.AccessLevelValue;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.*;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.AccountRegisterAsAdminDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.AccountInfoDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.AccountUpdatePasswordDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.AccountAccessLevelChangeDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.AccountRegisterDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.EditAccountInfoDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.service.AccountService;
import pl.lodz.p.it.ssbd2022.ssbd02.util.LoggingInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd02.security.AuthenticationContext;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.*;

@Stateful
@Interceptors(LoggingInterceptor.class)
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class AccountEndpoint {

    @Inject
    private AuthenticationContext authenticationContext;

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
        Account account = accountService.findByLogin(login);
        accountService.changeAccountStatus(account, false);
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
        Account account = accountService.findByLogin(login);
        accountService.changeAccountStatus(account, true);
    }

    /**
     * Konwertuje obiekt transferu danych użytkownika na obiekt klasy encji.
     *
     * @param accountRegisterDto Obiekt zawierający dane użytkownika
     * @throws IdenticalFieldException, Występuje w przypadku gdy rejestracja się nie powiedzie
     * @throws DatabaseException,       Występuje w przypadku gdy rejestracja się nie powiedzie
     */
    @PermitAll
    public void registerAccount(AccountRegisterDto accountRegisterDto)
            throws IdenticalFieldException, DatabaseException, DataNotFoundException {
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
    @RolesAllowed({ADMINISTRATOR})
    public void registerAccountByAdmin(AccountRegisterAsAdminDto accountRegisterAsAdminDto)
            throws IdenticalFieldException, DatabaseException, DataNotFoundException {
        Account account = accountRegisterDtoToAccount(accountRegisterAsAdminDto);
        account.setActive(accountRegisterAsAdminDto.getActive());
        account.setRegistered(accountRegisterAsAdminDto.getRegistered());
        accountService.registerAccountByAdmin(account);
    }

    @RolesAllowed({ADMINISTRATOR})
    public void updatePasswordAsAdmin(String login, AccountUpdatePasswordDto passwordDto) throws NoAccountFound {
        Account account = accountService.findByLogin(login);
        accountService.changeAccountPasswordAsAdmin(account, passwordDto.getPassword());
    }

    /**
     * Metoda pomocnicza konwertująca obiekt transferu danych na obiekt encji użytkownika
     *
     * @param accountRegisterDto Obiekt zawierający dane użytkownika
     * @return Obiekt klasy encji użytkownika
     */
    private Account accountRegisterDtoToAccount(AccountRegisterDto accountRegisterDto)
            throws IdenticalFieldException, DatabaseException, DataNotFoundException {
        Account account = new Account();
        account.setLogin(accountRegisterDto.getLogin());
        account.setPassword(accountRegisterDto.getPassword());
        account.setEmail(accountRegisterDto.getEmail());
        account.setName(accountRegisterDto.getName());
        account.setSurname(accountRegisterDto.getSurname());
        return account;
    }

    /**
     * Nadaje lub odbiera wskazany poziom dostępu w obiekcie klasy użytkownika.
     *
     * @param login Login użytkownika
     * @param data Obiekt zawierający informacje o zmienianym poziomie dostępu
     * @throws DataNotFoundException Wyjątek otrzymywany w przypadku próby dokonania operacji na niepoprawnej
     * nazwie poziomu dostępu lub próby ustawienia aktywnego/nieaktywnego już poziomu dostępu
     * @throws CannotChangeException Wyjątek otrzymywany w przypadku próby odebrania poziomu dostępu, którego użytkownik
     * nigdy nie posiadał
     * @see AccountAccessLevelChangeDto
     */
    @RolesAllowed({ADMINISTRATOR})
    public void changeAccountAccessLevel(String login, AccountAccessLevelChangeDto data)
            throws CannotChangeException, DataNotFoundException, NoAccountFound {
        Account account = accountService.findByLogin(login);
        AccessLevelValue accessLevelValue = accountService.findAccessLevelValueByName(data.getAccessLevel());
        accountService.changeAccountAccessLevel(account, accessLevelValue, data.getActive());
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
        Account account = authenticationContext.getCurrentUsersAccount();
        accountService.editAccountInfo(account, editAccountInfoDto);
    }

    /**
     * Wywołuję funkcję do edycji danych użytkownika przez administratora
     *
     * @param editAccountInfoDto klasa zawierająca zmienione dane danego użytkownika
     * @throws NoAccountFound W przypadku gdy nie znaleziono użytkownika o danym loginie
     */
    @RolesAllowed({ADMINISTRATOR})
    public void editAccountInfoAsAdmin(String login, EditAccountInfoDto editAccountInfoDto) throws NoAccountFound {
        // Można zwrócić użytkownika do userController w przyszłości, trzeba tylko opakować go w dto
        Account account = accountService.findByLogin(login);
        accountService.editAccountInfoAsAdmin(account, editAccountInfoDto);
    }

    /**
     * Zwraca informacje o dowolnym użytkowniku
     *
     * @param login nazwa użytkownika
     * @return obiekt DTO informacji o użytkowniku
     * @throws NoAccountFound              W przypadku gdy użytkownik o podanej nazwie nie istnieje lub
     *                                     gdy konto szukanego użytkownika jest nieaktywne, lub niepotwierdzone i
     *                                     informacje próbuje uzyskać użytkownik niebędący ani administratorem,
     *                                     ani moderatorem
     * @throws NoAuthenticatedAccountFound W przypadku gdy dane próbuje uzyskać niezalogowana osoba
     * @see AccountInfoDto
     */
    @RolesAllowed({ADMINISTRATOR, MODERATOR})
    public AccountInfoDto getAccountInfo(String login) throws NoAccountFound, NoAuthenticatedAccountFound {
        Account requester = authenticationContext.getCurrentUsersAccount();
        Account account = accountService.findByLogin(login);
        return new AccountInfoDto(accountService.getAccountInfo(requester, account));
    }

    /**
     * Zwraca informacje o zalogowanym użytkowniku
     *
     * @return obiekt DTO informacji o użytkowniku
     * @throws NoAuthenticatedAccountFound W przypadku gdy dane próbuje uzyskać niezalogowana osoba
     * @see AccountInfoDto
     */
    @RolesAllowed({ADMINISTRATOR, MODERATOR, CLIENT, PHOTOGRAPHER})
    public AccountInfoDto getOwnAccountInfo() throws NoAuthenticatedAccountFound {
        Account account = authenticationContext.getCurrentUsersAccount();
        return new AccountInfoDto(account);
    }

    @RolesAllowed({ADMINISTRATOR, MODERATOR, PHOTOGRAPHER, CLIENT})
    public void updateOwnPassword(AccountUpdatePasswordDto data) throws NoAuthenticatedAccountFound, PasswordMismatchException {
        Account account = authenticationContext.getCurrentUsersAccount();
        accountService.updateOwnPassword(account, data);
    }
}
