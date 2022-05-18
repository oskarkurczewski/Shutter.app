package pl.lodz.p.it.ssbd2022.ssbd02.mok.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.AccessLevelAssignment;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.AccessLevelValue;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.*;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.AccountInfoDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.AccountAccessLevelChangeDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.AccountUpdatePasswordDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.EditAccountInfoDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.facade.AuthenticationFacade;
import pl.lodz.p.it.ssbd2022.ssbd02.security.AuthenticationContext;
import pl.lodz.p.it.ssbd2022.ssbd02.util.LoggingInterceptor;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.util.List;
import java.util.stream.Collectors;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.*;

@Stateless
@Interceptors(LoggingInterceptor.class)
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AccountService {

    @Inject
    private AuthenticationFacade accountFacade;

    @Inject
    private AuthenticationContext authenticationContext;

    /**
     * Zmienia status użytkownika o danym loginie na podany
     *
     * @param login  login użytkownika, dla którego ma zostać dokonana zmiana statusu
     * @param active status, który ma zostać ustawiony
     * @throws NoAccountFound, kiedy użytkownik o danym loginie nie zostanie odnaleziony
     *                         w bazie danych
     */
    @RolesAllowed({blockAccount, unblockAccount})
    public void changeAccountStatus(String login, Boolean active) throws NoAccountFound {
        Account account = accountFacade.findByLogin(login);
        account.setActive(active);
        accountFacade.getEm().merge(account); // TODO Po implementacji transakcyjności zmineić na wywołanie metody update fasady
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
    @RolesAllowed({"ADMINISTRATOR", "MODERATOR", "CLIENT", "PHOTOGRAPHER"})
    public AccountInfoDto getAccountInfo(String login) throws NoAccountFound, NoAuthenticatedAccountFound {
        Account authenticatedAccount = authenticationContext.getCurrentUsersAccount();
        List<String> accesLevelList = authenticatedAccount
                .getAccessLevelAssignmentList()
                .stream()
                .filter(AccessLevelAssignment::getActive)
                .map(a -> a.getLevel().getName())
                .collect(Collectors.toList());

        Account account = accountFacade.findByLogin(login);
        if (Boolean.TRUE.equals(!account.getActive()) || Boolean.TRUE.equals(!account.getRegistered())) {
            if (accesLevelList.contains("ADMINISTRATOR") || accesLevelList.contains("MODERATOR")) {
                return new AccountInfoDto(account);
            } else {
                throw ExceptionFactory.noAccountFound();
            }
        } else {
            return new AccountInfoDto(account);
        }
    }

    /**
     * Zwraca informacje o zalogowanym użytkowniku
     *
     * @return obiekt DTO informacji o użytkowniku
     * @throws NoAuthenticatedAccountFound W przypadku gdy dane próbuje uzyskać niezalogowana osoba
     * @see AccountInfoDto
     */
    @RolesAllowed({"ADMINISTRATOR", "MODERATOR", "CLIENT", "PHOTOGRAPHER"})
    public AccountInfoDto getYourAccountInfo() throws NoAuthenticatedAccountFound {
        Account account = authenticationContext.getCurrentUsersAccount();
        return new AccountInfoDto(account);
    }

    /**
     * Metoda pozwalająca administratorowi zmienić hasło dowolnego użytkowika
     *
     * @param accountId ID użytkownika, którego hasło administrator chce zmienić
     * @param data      obiekt zawierający nowe hasło dla wskazanego użytkownika
     */
    @RolesAllowed(changeSomeonesPassword)
    public void changeAccountPasswordAsAdmin(Long accountId, AccountUpdatePasswordDto data) {
        Account target = accountFacade.find(accountId);
        changePassword(target, data.getPassword());
    }

    /**
     * Metoda pozwalająca zmienić własne hasło
     *
     * @param data obiekt zawierający stare hasło (w celu weryfikacji) oraz nowe mające być ustawione dla użytkownika
     */
    @RolesAllowed(changeOwnPassword)
    public void updateOwnPassword(AccountUpdatePasswordDto data) throws NoAuthenticatedAccountFound {
        if (data.getOldPassword() == null) {
            throw ExceptionFactory.wrongPasswordException();
        }
        Account current = authenticationContext.getCurrentUsersAccount();
        String oldHash = BCrypt.withDefaults().hashToString(6, data.getOldPassword().toCharArray());
        if (!oldHash.equals(current.getPassword())) {
            throw ExceptionFactory.passwordMismatchException();
        }
        changePassword(current, data.getPassword());
    }

    /**
     * Pomocnicza metoda utworzone w celu uniknięcia powtarzania kodu.
     * Zmienia hasło wskazanego użytkownika
     *
     * @param target      ID użytkownika, którego modyfikujemy
     * @param newPassword nowe hasło dla użytkownika
     */
    private void changePassword(Account target, String newPassword) {
        if (newPassword.trim().length() < 8) {
            throw ExceptionFactory.wrongPasswordException();
        }
        String hashed = BCrypt.withDefaults().hashToString(6, newPassword.toCharArray());
        target.setPassword(hashed);
        accountFacade.update(target);
    }

    /**
     * Nadaje lub odbiera wskazany poziom dostępu w obiekcie klasy użytkownika.
     *
     * @param accountId Identyfikator konta użytkownika
     * @param accessLevel Łańcuch znaków zawierający nazwę poziomu dostępu
     * @param active status poziomu dostępu, który ma być ustawiony
     * @throws DataNotFoundException Wyjątek otrzymywany w przypadku próby dokonania operacji na niepoprawnej
     * nazwie poziomu dostępu lub próby ustawienia aktywnego/nieaktywnego już poziomu dostępu
     * @throws CannotChangeException Wyjątek otrzymywany w przypadku próby odebrania poziomu dostępu, którego użytkownik
     * nigdy nie posiadał
     * @see AccountAccessLevelChangeDto
     */
    @RolesAllowed({"ADMINISTRATOR"})
    public void changeAccountAccessLevel(Long accountId, String accessLevel, Boolean active)
            throws DataNotFoundException, CannotChangeException {

        Account target = accountFacade.find(accountId);
        List<AccessLevelAssignment> accountAccessLevels = target.getAccessLevelAssignmentList();
        AccessLevelAssignment accessLevelFound = accountFacade.getAccessLevelAssignmentForAccount(accountId, accessLevel);

        if(accessLevelFound != null) {
            if (accessLevelFound.getActive() == active) {
                throw new CannotChangeException("exception.access_level.already_set");
            }

            accessLevelFound.setActive(active);
        } else {
            AccessLevelValue accessLevelValue = accountFacade.getAccessLevelValue(accessLevel);
            AccessLevelAssignment assignment = new AccessLevelAssignment();

            if (!active) {
                throw new CannotChangeException("exception.access_level.already_false");
            }

            assignment.setLevel(accessLevelValue);
            assignment.setAccount(target);
            assignment.setActive(active);
            accountAccessLevels.add(assignment);

            target.setAccessLevelAssignmentList(accountAccessLevels);
        }

        accountFacade.update(target);
    }

    /**
     * Rejestruje konto użytkownika z danych podanych w obiekcie klasy użytkownika
     * oraz przypisuje do niego poziom dostępu klienta.
     * W celu aktywowania konta należy jeszcze zmienić pole 'registered' na wartość 'true'.
     *
     * @param account Obiekt klasy Account reprezentującej dane użytkownika
     * @throws IdenticalFieldException Wyjątek otrzymywany w przypadku niepowodzenia rejestracji (login lub adres email już istnieje)
     * @see Account
     */
    @PermitAll
    public void registerAccount(Account account) throws IdenticalFieldException, DataNotFoundException, DatabaseException {
        account.setPassword(BCrypt.withDefaults().hashToString(6, account.getPassword().toCharArray()));
        account.setActive(true);
        account.setRegistered(false);

        List<AccessLevelAssignment> list = addClientAccessLevel(account);
        account.setAccessLevelAssignmentList(list);

        accountFacade.registerAccount(account);
    }

    /**
     * Rejestruje konto użytkownika z danych podanych w obiekcie klasy użytkownika (wraz z polami registered i active)
     * oraz przypisuje do niego poziom dostępu klienta.
     *
     * @param account Obiekt klasy Account reprezentującej dane użytkownika
     * @throws IdenticalFieldException Wyjątek otrzymywany w przypadku niepowodzenia rejestracji (login lub adres email już istnieje)
     * @see Account
     */
    @RolesAllowed({"ADMINISTRATOR"})
    public void registerAccountByAdmin(Account account) throws IdenticalFieldException, DatabaseException, DataNotFoundException {
        account.setPassword(BCrypt.withDefaults().hashToString(6, account.getPassword().toCharArray()));

        List<AccessLevelAssignment> list = addClientAccessLevel(account);
        account.setAccessLevelAssignmentList(list);

        accountFacade.registerAccount(account);
    }

    /**
     * Metoda pomocnicza tworząca wpis o poziomie dostępu klient dla danego użytkownika.
     *
     * @param account Obiekt klasy Account reprezentującej dane użytkownika
     * @return Lista poziomów dostępu użytkownika
     */
    private List<AccessLevelAssignment> addClientAccessLevel(Account account) throws DataNotFoundException {
        AccessLevelValue levelValue = accountFacade.getAccessLevelValue("CLIENT");
        AccessLevelAssignment assignment = new AccessLevelAssignment();

        assignment.setLevel(levelValue);
        assignment.setAccount(account);
        assignment.setActive(true);

        List<AccessLevelAssignment> list = account.getAccessLevelAssignmentList();
        list.add(assignment);

        return list;
    }

    /**
     * Funkcja do edycji danych użytkownika. Zmienia tylko proste informacje, a nie role dostępu itp
     *
     * @param editAccountInfoDto klasa zawierająca zmienione dane danego użytkownika
     * @return obiekt użytkownika po aktualizacji
     * @throws NoAuthenticatedAccountFound W przypadku gdy nie znaleziono aktualnego użytkownika
     */
    @RolesAllowed(editOwnAccountData)
    public Account editAccountInfo(EditAccountInfoDto editAccountInfoDto) throws NoAuthenticatedAccountFound {
        Account account = authenticationContext.getCurrentUsersAccount();
        account.setEmail(editAccountInfoDto.getEmail());
        account.setName(editAccountInfoDto.getName());
        account.setSurname(editAccountInfoDto.getSurname());
        return accountFacade.update(account);
    }

    /**
     * Funkcja do edycji danych innego użytkownika przez Administratora. Pozwala zmienić jedynie email, imię oraz nazwisko
     *
     * @param editAccountInfoDto klasa zawierająca zmienione dane danego użytkownika
     * @return obiekt użytkownika po aktualizacji
     * @throws NoAccountFound W przypadku gdy nie znaleziono użytkownika o danym loginie
     */
    @RolesAllowed({"ADMINISTRATOR"})
    public Account editAccountInfoAsAdmin(String login, EditAccountInfoDto editAccountInfoDto) throws NoAccountFound {
        Account account = accountFacade.findByLogin(login);
        account.setEmail(editAccountInfoDto.getEmail());
        account.setName(editAccountInfoDto.getName());
        account.setSurname(editAccountInfoDto.getSurname());
        return accountFacade.update(account);
    }
}
