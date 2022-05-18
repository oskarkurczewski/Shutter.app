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

    /**
     * Odnajduje konto użytkownika o podanym loginie
     *
     * @param login Login użytkownika, którego konta ma być wyszukane
     * @throws NoAccountFound W przypadku nieznalezienia konta
     */
    @PermitAll
    public Account findByLogin(String login) throws  NoAccountFound {
        return accountFacade.findByLogin(login);
    }

    /**
     * Odnajduje wybraną wartość poziomu dostępu na bazie jej nazwy
     *
     * @param name Nazwa poziomu dostępu
     * @throws DataNotFoundException W momencie, gdy dany poziom dostępu nie zostanie odnaleziony
     */
    @PermitAll
    public AccessLevelValue findAccessLevelValueByName(String name) throws DataNotFoundException {
        return accountFacade.getAccessLevelValue(name);
    }

    /**
     * Zmienia status użytkownika o danym loginie na podany
     *
     * @param account użytkownika, dla którego ma zostać dokonana zmiana statusu
     * @param active  status, który ma zostać ustawiony
     */
    @RolesAllowed({blockAccount, unblockAccount})
    public void changeAccountStatus(Account account, Boolean active) {
        account.setActive(active);
        accountFacade.update(account);
    }

    /**
     * Szuka użytkownika
     *
     * @param requester konto użytkownika, który chce uzyskać informacje o danym koncie
     * @param account konto użytkownika, którego dane mają zostać pozyskane
     * @return obiekt DTO informacji o użytkowniku
     * @throws NoAccountFound              W przypadku gdy użytkownik o podanej nazwie nie istnieje lub
     *                                     gdy konto szukanego użytkownika jest nieaktywne, lub niepotwierdzone
     *                                     i informacje próbuje uzyskać użytkownik niebędący ani administratorem,
     *                                     ani moderatorem
     * @see AccountInfoDto
     */
    @RolesAllowed({ADMINISTRATOR, MODERATOR, PHOTOGRAPHER, CLIENT})
    public Account getAccountInfo(Account requester, Account account) throws NoAccountFound {
        List<String> accessLevelList = requester
                .getAccessLevelAssignmentList()
                .stream()
                .filter(AccessLevelAssignment::getActive)
                .map(a -> a.getLevel().getName())
                .collect(Collectors.toList());
        if (Boolean.TRUE.equals(account.getActive()) && Boolean.TRUE.equals(account.getRegistered())) {
            return account;
        }
        if (accessLevelList.contains(ADMINISTRATOR) || accessLevelList.contains(MODERATOR)) {
            return account;
        }
        throw ExceptionFactory.noAccountFound();
    }

    /**
     * Metoda pozwalająca administratorowi zmienić hasło dowolnego użytkowika
     *
     * @param account   Użytkownik, którego hasło administrator chce zmienić
     * @param password  Nowe hasło dla wskazanego użytkownika
     */
    @RolesAllowed(changeSomeonesPassword)
    public void changeAccountPasswordAsAdmin(Account account, String password) {
        changePassword(account, password);
    }

    /**
     * Metoda pozwalająca zmienić własne hasło
     *
     * @param data obiekt zawierający stare hasło (w celu weryfikacji) oraz nowe mające być ustawione dla użytkownika
     */
    @RolesAllowed(changeOwnPassword)
    public void updateOwnPassword(Account account, AccountUpdatePasswordDto data) {
        if (data.getOldPassword() == null) {
            throw ExceptionFactory.wrongPasswordException();
        }
        String oldHash = BCrypt.withDefaults().hashToString(6, data.getOldPassword().toCharArray());
        if (!oldHash.equals(account.getPassword())) {
            throw ExceptionFactory.passwordMismatchException();
        }
        changePassword(account, data.getPassword());
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
     * @param account                   Konto użytkownika, dla którego ma nastąpić zmiana poziomu dostępu
     * @param accessLevelValue          Poziom dostępu który ma zostać zmieniony dla użytkownika
     * @param active                    Status poziomu dostępu, który ma być ustawiony
     * @throws CannotChangeException    W przypadku próby odebrania poziomu dostępu, którego użytkownik nigdy nie posiadał
     * @see AccountAccessLevelChangeDto
     */
    @RolesAllowed({ADMINISTRATOR})
    public void changeAccountAccessLevel(Account account, AccessLevelValue accessLevelValue, Boolean active)
            throws CannotChangeException {

        List<AccessLevelAssignment> accountAccessLevels = account.getAccessLevelAssignmentList();
        AccessLevelAssignment accessLevelFound = accountFacade.getAccessLevelAssignmentForAccount(
                account,
                accessLevelValue
        );

        if(accessLevelFound != null) {
            if (accessLevelFound.getActive() == active) {
                throw new CannotChangeException("exception.access_level.already_set");
            }

            accessLevelFound.setActive(active);
        } else {
            AccessLevelAssignment assignment = new AccessLevelAssignment();

            if (!active) {
                throw new CannotChangeException("exception.access_level.already_false");
            }

            assignment.setLevel(accessLevelValue);
            assignment.setAccount(account);
            assignment.setActive(active);
            accountAccessLevels.add(assignment);

            account.setAccessLevelAssignmentList(accountAccessLevels);
        }

        accountFacade.update(account);
    }

    /**
     * Rejestruje konto użytkownika z danych podanych w obiekcie klasy użytkownika
     * oraz przypisuje do niego poziom dostępu klienta.
     * W celu aktywowania konta należy jeszcze zmienić pole 'registered' na wartość 'true'.
     *
     * @param account Obiekt klasy Account reprezentującej dane użytkownika
     * @throws IdenticalFieldException Wyjątek otrzymywany w przypadku niepowodzenia rejestracji
     *                                 (login lub adres email już istnieje)
     * @see Account
     */
    @PermitAll
    public void registerAccount(Account account)
            throws IdenticalFieldException, DataNotFoundException, DatabaseException {
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
     * @throws IdenticalFieldException Wyjątek otrzymywany w przypadku niepowodzenia rejestracji
     *                                 (login lub adres email już istnieje)
     * @see Account
     */
    @RolesAllowed({ADMINISTRATOR})
    public void registerAccountByAdmin(Account account)
            throws IdenticalFieldException, DatabaseException, DataNotFoundException {
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
        AccessLevelValue levelValue = accountFacade.getAccessLevelValue(CLIENT);
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
     */
    @RolesAllowed(editOwnAccountData)
    public Account editAccountInfo(Account account, EditAccountInfoDto editAccountInfoDto) {
        account.setEmail(editAccountInfoDto.getEmail());
        account.setName(editAccountInfoDto.getName());
        account.setSurname(editAccountInfoDto.getSurname());
        return accountFacade.update(account);
    }

    /**
     * Funkcja do edycji danych innego użytkownika przez Administratora. Pozwala zmienić jedynie email,
     * imię oraz nazwisko
     *
     * @param editAccountInfoDto klasa zawierająca zmienione dane danego użytkownika
     * @return obiekt użytkownika po aktualizacji
     */
    @RolesAllowed({ADMINISTRATOR})
    public Account editAccountInfoAsAdmin(Account account, EditAccountInfoDto editAccountInfoDto) {
        account.setEmail(editAccountInfoDto.getEmail());
        account.setName(editAccountInfoDto.getName());
        account.setSurname(editAccountInfoDto.getSurname());
        return accountFacade.update(account);
    }
}
