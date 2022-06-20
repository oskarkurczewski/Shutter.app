package pl.lodz.p.it.ssbd2022.ssbd02.mok.service;

import org.hibernate.exception.ConstraintViolationException;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.*;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.*;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.*;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.facade.AccessLevelFacade;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.facade.AccountChangeLogFacade;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.facade.AccountListPreferencesFacade;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.facade.AuthenticationFacade;
import pl.lodz.p.it.ssbd2022.ssbd02.security.AuthenticationContext;
import pl.lodz.p.it.ssbd2022.ssbd02.security.BCryptUtils;
import pl.lodz.p.it.ssbd2022.ssbd02.security.OneTimeCodeUtils;
import pl.lodz.p.it.ssbd2022.ssbd02.util.ConfigLoader;
import pl.lodz.p.it.ssbd2022.ssbd02.util.EmailService;
import pl.lodz.p.it.ssbd2022.ssbd02.util.LoggingInterceptor;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.PersistenceException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.*;
import static pl.lodz.p.it.ssbd2022.ssbd02.util.ConstraintNames.IDENTICAL_EMAIL;
import static pl.lodz.p.it.ssbd2022.ssbd02.util.ConstraintNames.IDENTICAL_LOGIN;

@Stateless
@Interceptors(LoggingInterceptor.class)
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AccountService {

    @Inject
    private AuthenticationFacade accountFacade;

    @Inject
    private AccessLevelFacade accessLevelFacade;

    @Inject
    private VerificationTokenService verificationTokenService;

    @Inject
    private AccountListPreferencesFacade accountListPreferencesFacade;

    @Inject
    private EmailService emailService;

    @Inject
    private OneTimeCodeUtils codeUtils;

    @Inject
    private AccountChangeLogFacade accountChangeLogFacade;

    @Inject
    private ConfigLoader configLoader;

    /**
     * Odnajduje konto użytkownika o podanym loginie
     *
     * @param login Login użytkownika, którego konta ma być wyszukane
     * @throws NoAccountFound Konto o podanej nazwie nie istnieje
     */
    @PermitAll
    public Account findByLogin(String login) throws BaseApplicationException {
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
        return accessLevelFacade.getAccessLevelValue(name);
    }

    /**
     * Zmienia status użytkownika o danym loginie na podany
     *
     * @param account użytkownika, dla którego ma zostać dokonana zmiana statusu
     * @param active  status, który ma zostać ustawiony
     */
    @RolesAllowed({blockAccount, unblockAccount})
    public void changeAccountStatus(Account account, Boolean active) throws BaseApplicationException {
        account.setActive(active);

        if (!active) {
            emailService.sendAccountBlocked(account.getEmail(), account.getLocale());
        } else {
            emailService.sendAccountUnblockedEmail(account.getEmail(), account.getLocale());
        }
        accountFacade.update(account);
    }

    /**
     * Potwierdza aktywację własnego konta po długim czasie nieaktywności
     *
     * @param token Obiekt przedstawiający żeton weryfikacyjny użyty do aktywacji konta
     * @throws BaseApplicationException Występuje w przypadku gdy aktywacja konta się nie powiedzie
     */
    @PermitAll
    public void confirmUnblockOwnAccount(String token) throws BaseApplicationException {
        Account account = verificationTokenService.confirmUnblockOwnAccount(token);
        account.setActive(true);
        accountFacade.update(account);
    }

    /**
     * Szuka użytkownika
     *
     * @param account konto użytkownika, którego dane mają zostać pozyskane
     * @return obiekt DTO informacji o użytkowniku
     * @throws NoAccountFound Konto o podanej nazwie nie istnieje w systemie lub jest niepotwierdzone/zablokowane
     * @see Account
     */
    @RolesAllowed(getAccountInfo)
    public Account getAccountInfo(Account account) throws NoAccountFound {
        if (Boolean.TRUE.equals(account.getActive()) && Boolean.TRUE.equals(account.getRegistered())) {
            return account;
        } else {
            throw ExceptionFactory.noAccountFound();
        }
    }


    /**
     * Metoda pozwalająca administratorowi zmienić hasło dowolnego użytkowika
     *
     * @param account  Użytkownik, którego hasło administrator chce zmienić
     * @param password Nowe hasło dla wskazanego użytkownika
     */
    @RolesAllowed(changeSomeonesPassword)
    public void changeAccountPasswordAsAdmin(Account account, String password) throws BaseApplicationException {
        changePassword(account, password);
        verificationTokenService.sendForcedPasswordResetToken(account);
    }

    /**
     * Metoda pozwalająca zmienić własne hasło
     *
     * @param data obiekt zawierający stare hasło (w celu weryfikacji) oraz nowe mające być ustawione dla użytkownika
     */
    @RolesAllowed(changeOwnPassword)
    public void updateOwnPassword(Account account, AccountUpdatePasswordDto data) throws BaseApplicationException {
        if (data.getOldPassword() == null) {
            throw ExceptionFactory.wrongPasswordException();
        }
        if (!BCryptUtils.verify(data.getOldPassword().toCharArray(), account.getPassword())) {
            throw ExceptionFactory.passwordMismatchException();
        }

        changePassword(account, data.getPassword());
    }

    /**
     * Pomocnicza metoda utworzone w celu uniknięcia powtarzania kodu.
     * Zmienia hasło wskazanego użytkownika
     *
     * @param target      Obiekt użytkownika, którego modyfikujemy
     * @param newPassword nowe hasło dla użytkownika
     */
    private void changePassword(Account target, String newPassword) throws BaseApplicationException {
        if (newPassword.trim().length() < 8) {
            throw ExceptionFactory.wrongPasswordException();
        }
        String hashed = BCryptUtils.generate(newPassword.toCharArray());
        if (!isPasswordUniqueForUser(hashed, target)) {
            throw ExceptionFactory.nonUniquePasswordException();
        }
        target.setPassword(hashed);
        accountFacade.update(target);
    }

    /**
     * Resetuje hasło użytkownika na podane pod warunkiem, że żeton weryfikujący jest aktualny oraz poprawny
     *
     * @param resetPasswordDto Informacje wymagane do resetu hasła (żeton oraz nowe hasło)
     * @throws InvalidTokenException    Żeton jest nieprawidłowego typu lub nieaktualny
     * @throws NoVerificationTokenFound Żeton nie zostanie odnaleziony w bazie
     * @throws ExpiredTokenException    Żeton wygasł
     */
    @PermitAll
    public void resetPassword(ResetPasswordDto resetPasswordDto) throws BaseApplicationException {
        Account account = verificationTokenService.confirmPasswordReset(resetPasswordDto.getToken());
        changePassword(account, resetPasswordDto.getNewPassword());
    }

    /**
     * Aktualizuje adres email użytkownika na podane pod warunkiem, że żeton weryfikujący jest aktualny oraz poprawny
     *
     * @param emailUpdateDto Informacje do zmiany emaila użytkownika
     * @throws InvalidTokenException    Żeton jest nieprawidłowego typu lub nieaktualny
     * @throws NoVerificationTokenFound Żeton nie zostanie odnaleziony w bazie
     * @throws ExpiredTokenException    Żeton wygasł
     */
    @RolesAllowed((updateEmail))
    public void updateEmail(EmailUpdateDto emailUpdateDto) throws BaseApplicationException {
        Account account = verificationTokenService.confirmEmailUpdate(emailUpdateDto.getToken());
        account.setEmail(emailUpdateDto.getNewEmail());
        accountFacade.update(account);
    }

    /**
     * Nadaje lub odbiera wskazany poziom dostępu w obiekcie klasy użytkownika.
     *
     * @param account          Konto użytkownika, dla którego ma nastąpić zmiana poziomu dostępu
     * @param accessLevelValue Poziom dostępu, który ma zostać zmieniony dla użytkownika
     * @param active           Status poziomu dostępu, który ma być ustawiony
     * @throws CannotChangeException W przypadku próby odebrania poziomu dostępu, którego użytkownik nigdy nie posiadał
     * @see AccountAccessLevelChangeDto
     */
    @RolesAllowed({grantAccessLevel, revokeAccessLevel, becomePhotographer})
    public void changeAccountAccessLevel(Account account, AccessLevelValue accessLevelValue, Boolean active)
            throws BaseApplicationException {

        AccessLevelAssignment accessLevelFound = accessLevelFacade.getAccessLevelAssignmentForAccount(
                account,
                accessLevelValue
        );

        if (accessLevelFound != null) {
            if (accessLevelFound.getActive() == active) {
                throw new CannotChangeException("exception.access_level.already_set");
            }

            accessLevelFound.setActive(active);

            sendAccessLevelChangeMail(accessLevelValue, account, active);
            accessLevelFacade.update(accessLevelFound);
        } else {
            AccessLevelAssignment assignment = new AccessLevelAssignment();

            if (!active) {
                throw new CannotChangeException("exception.access_level.already_false");
            }

            assignment.setLevel(accessLevelValue);
            assignment.setAccount(account);
            assignment.setActive(active);

            sendAccessLevelChangeMail(accessLevelValue, account, true);
            accessLevelFacade.persist(assignment);
        }
    }

    /**
     * Metoda pomocnicza służąca do wysyłania powiadomień o zmianach poziomu dostępu użytkownika
     *
     * @param accessLevelValue wartość poziomu dostępu
     * @param account          konto dla, którego poziom dostępu został zmieniony
     * @param active           określa czy zmiana stanowiła przyznanie, czy odebranie poziomu dostępu
     */
    private void sendAccessLevelChangeMail(AccessLevelValue accessLevelValue, Account account, Boolean active) {
        if (active) {
            emailService.sendAccessLevelGrantedEmail(
                    account.getEmail(),
                    account.getLocale(),
                    accessLevelValue.getName()
            );
        } else {
            emailService.sendAccessLevelRevokedEmail(
                    account.getEmail(),
                    account.getLocale(),
                    accessLevelValue.getName()
            );
        }
    }

    /**
     * Ustawia poziom dostępu fotografa w obiekcie klasy użytkownika na aktywny.
     *
     * @param account Konto użytkownika, dla którego ma nastąpić nadanie roli fotografa
     * @throws CannotChangeException W przypadku próby zostania fotografem przez uzytkownika mającego już tę rolę
     */
    @RolesAllowed(becomePhotographer)
    public void becomePhotographer(Account account)
            throws BaseApplicationException {

        AccessLevelAssignment accessLevelFound = accessLevelFacade.getAccessLevelAssignmentForAccount(
                account,
                accessLevelFacade.getAccessLevelValue("PHOTOGRAPHER")
        );

        if (accessLevelFound != null) {
            if (accessLevelFound.getActive()) {
                throw ExceptionFactory.cannotChangeException();
            }

            accessLevelFound.setActive(true);
            accessLevelFacade.update(accessLevelFound);
        } else {
            AccessLevelAssignment assignment = new AccessLevelAssignment();

            assignment.setLevel(accessLevelFacade.getAccessLevelValue("PHOTOGRAPHER"));
            assignment.setAccount(account);
            assignment.setActive(true);

            accessLevelFacade.persist(assignment);
        }
    }


    /**
     * Odbiera rolę fotografa poprzez ustawienie poziomu dostępu fotografa w obiekcie klasy użytkownika na nieaktywny.
     *
     * @param account Konto użytkownika, dla którego ma nastąpić odebranie roli fotografa
     * @throws CannotChangeException W przypadku próby zaprzestania bycia fotografem przez uzytkownika mającego
     *                               tę rolę nieaktywną bądź wcale jej niemającego
     */

    @RolesAllowed(stopBeingPhotographer)
    public void stopBeingPhotographer(Account account) throws BaseApplicationException {
        AccessLevelAssignment accessLevelFound = accessLevelFacade.getAccessLevelAssignmentForAccount(
                account,
                accessLevelFacade.getAccessLevelValue("PHOTOGRAPHER")
        );

        if (accessLevelFound != null) {
            if (accessLevelFound.getActive()) {
                accessLevelFound.setActive(false);
                accessLevelFacade.update(accessLevelFound);
            } else {
                throw ExceptionFactory.cannotChangeException();
            }
        } else {
            throw ExceptionFactory.cannotChangeException();
        }

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
    public void registerOwnAccount(Account account)
            throws BaseApplicationException {
        account.setPassword(BCryptUtils.generate(account.getPassword().toCharArray()));
        account.setTwoFAEnabled(false);
        account.setActive(true);
        account.setRegistered(false);
        account.setFailedLogInAttempts(0);
        account.setSecret(UUID.randomUUID().toString());

        addNewAccount(account);

        verificationTokenService.sendRegistrationToken(account);
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
    @RolesAllowed({createAccount})
    public void registerAccountByAdmin(Account account)
            throws BaseApplicationException {
        account.setPassword(BCryptUtils.generate(account.getPassword().toCharArray()));
        account.setTwoFAEnabled(false);
        account.setFailedLogInAttempts(0);
        account.setLocale(Locale.forLanguageTag("en"));
        account.setSecret(UUID.randomUUID().toString());

        addNewAccount(account);

        if (!account.getRegistered()) {
            verificationTokenService.sendRegistrationToken(account);
        } else {
            addClientAccessLevel(account);
        }
    }

    /**
     * Tworzy konto użytkownika w bazie danych,
     * w przypadku naruszenia unikatowości loginu lub adresu email otrzymujemy wyjątek
     *
     * @param account obiekt encji użytkownika
     * @throws IdenticalFieldException W przypadku, gdy login lub adres email już się znajduje w bazie danych
     */
    private void addNewAccount(Account account) throws BaseApplicationException {
        try {
            accountFacade.persist(account);
        } catch (PersistenceException ex) {
            if (ex.getCause() instanceof ConstraintViolationException) {
                String name = ((ConstraintViolationException) ex.getCause()).getConstraintName();
                switch (name) {
                    case IDENTICAL_LOGIN:
                        throw ExceptionFactory.identicalFieldException("exception.identical_login");
                    case IDENTICAL_EMAIL:
                        throw ExceptionFactory.identicalFieldException("exception.identical_email");
                }
            }
            throw ExceptionFactory.databaseException();
        }
    }

    /**
     * Metoda pomocnicza tworząca wpis o poziomie dostępu klient dla danego użytkownika.
     *
     * @param account Obiekt klasy Account reprezentującej dane użytkownika
     */
    private void addClientAccessLevel(Account account) throws BaseApplicationException {
        AccessLevelValue levelValue = accessLevelFacade.getAccessLevelValue(CLIENT);

        AccessLevelAssignment assignment = new AccessLevelAssignment();

        assignment.setLevel(levelValue);
        assignment.setAccount(account);
        assignment.setActive(true);

        accessLevelFacade.persist(assignment);
    }

    /**
     * Potwierdza rejestracje konta ustawiając pole 'registered' na wartość 'true'
     *
     * @param token Obiekt przedstawiający żeton weryfikacyjny użyty do potwierdzenia rejestracji
     * @throws BaseApplicationException Występuje w przypadku gdy potwierdzenie rejestracji się nie powiedzie
     */
    @PermitAll
    public void confirmAccountRegistration(String token) throws BaseApplicationException {
        Account account = verificationTokenService.confirmRegistration(token);
        account.setRegistered(true);
        addClientAccessLevel(account);
        accountFacade.update(account);
        emailService.sendAccountActivated(account.getEmail(), account.getLocale());
    }

    /**
     * Funkcja do edycji danych użytkownika. Zmienia tylko proste informacje, a nie role dostępu itp
     *
     * @param editAccountInfoDto klasa zawierająca zmienione dane danego użytkownika
     */
    @RolesAllowed(editOwnAccountData)
    public void editAccountInfo(Account account, EditAccountInfoDto editAccountInfoDto) throws BaseApplicationException {
        account.setName(editAccountInfoDto.getName());
        account.setSurname(editAccountInfoDto.getSurname());
        accountFacade.update(account);
    }

    /**
     * Funkcja do edycji danych innego użytkownika przez Administratora. Pozwala zmienić jedynie email,
     * imię oraz nazwisko
     *
     * @param editAccountInfoAsAdminDto klasa zawierająca zmienione dane danego użytkownika
     */
    @RolesAllowed({editSomeonesAccountData})
    public void editAccountInfoAsAdmin(Account account, EditAccountInfoAsAdminDto editAccountInfoAsAdminDto) throws BaseApplicationException {
        account.setEmail(editAccountInfoAsAdminDto.getEmail());
        account.setName(editAccountInfoAsAdminDto.getName());
        account.setSurname(editAccountInfoAsAdminDto.getSurname());
        account.setActive(editAccountInfoAsAdminDto.getActive());
        accountFacade.update(account);
    }

    /**
     * Zwraca listę wszystkich użytkowników w zadanej kolejności spełniających warunki zapytania
     *
     * @param requestDto obiekt DTO zawierający informacje o sortowaniu i filtrowaniu
     * @return lista użytkowników
     */
    @RolesAllowed(listAllAccounts)
    public ListResponseDto<TableAccountDto> getAccountList(Account requester, AccountListRequestDto requestDto) {
        List<Account> list = accountFacade.getAccountList(
                requestDto.getPage(),
                requestDto.getRecordsPerPage(),
                requestDto.getOrderBy(),
                requestDto.getOrderAsc(),
                requestDto.getLogin(),
                requestDto.getEmail(),
                requestDto.getName(),
                requestDto.getSurname(),
                requestDto.getRegistered(),
                requestDto.getActive()
        );
        Long allRecords = accountFacade.getAccountListSize(
                requestDto.getLogin(),
                requestDto.getEmail(),
                requestDto.getName(),
                requestDto.getSurname(),
                requestDto.getRegistered(),
                requestDto.getActive()
        );

        return new ListResponseDto<>(
                requestDto.getPage(),
                (int) Math.ceil(allRecords.doubleValue() / requestDto.getRecordsPerPage()),
                requestDto.getRecordsPerPage(),
                allRecords,
                list.stream().map(TableAccountDto::new).collect(Collectors.toList())
        );
    }

    /**
     * Zapisuje preferencje wyświetlania listy kont użytkownika
     *
     * @param account konto użytkownika, dla którego mają zostać zapisane preferencje
     */
    @RolesAllowed(listAllAccounts)
    public void saveAccountListPreferences(
            Account account,
            int page,
            int recordsPerPage,
            String orderBy,
            Boolean orderAsc
    ) throws BaseApplicationException {
        try {
            AccountListPreferences accountListPreferences = accountListPreferencesFacade.findByAccount(account);
            savePreferences(accountListPreferences, account, page, recordsPerPage, orderBy, orderAsc);
            accountListPreferencesFacade.update(accountListPreferences);
        } catch (NoAccountListPreferencesFound e) {
            AccountListPreferences accountListPreferences = new AccountListPreferences();
            savePreferences(accountListPreferences, account, page, recordsPerPage, orderBy, orderAsc);
            accountListPreferencesFacade.persist(accountListPreferences);
        }
    }


    /**
     * Metoda pomocnicza zapisująca preferencje wyświetlania listy kont użytkownika
     *
     * @param preferences obiekt preferencji, do którego preferencje mają zostać zapisane
     * @param account     konto użytkownika, dla którego mają zostać zapisane preferencje
     */
    private void savePreferences(
            AccountListPreferences preferences,
            Account account,
            int page,
            int recordsPerPage,
            String orderBy,
            Boolean orderAsc
    ) {
        preferences.setAccount(account);
        preferences.setOrderAsc(orderAsc);
        preferences.setOrderBy(orderBy);
        preferences.setPage(page);
        preferences.setRecordsPerPage(recordsPerPage);
    }

    /**
     * Zwraca ostatnio ustawione w dla danego użytkownika preferencje sortowania oraz stronicowania list kont
     *
     * @param account konto, dla którego należy zwrócić preferencje
     * @throws BaseApplicationException preferencje dla danego użytkownika nie zostaną odnalezione
     */
    @RolesAllowed(listAllAccounts)
    public AccountListPreferences getAccountListPreferences(Account account) throws BaseApplicationException {
        return accountListPreferencesFacade.findByAccount(account);
    }

    /**
     * Rejestruje nieudane logowanie na konto użytkownika poprzez inkrementację licznika nieudanych
     * logowań jego konta. Jeżeli liczba nieudanych logowań będzie równa lub większa od 3, to konto zostaje
     * automatycznie zablokowane, a użytkownik zostaje powiadomiony o tym drogą mailową.
     *
     * @param account Konto, dla którego należy zarejestrować nieudaną operację logowania
     */
    @PermitAll
    public void registerFailedLogInAttempt(Account account, String ipAddress) {
        account.setLastFailedLogInAttempt(LocalDateTime.now());
        account.setLastFailedLoginIp(ipAddress);
        if (!account.getActive() || !account.getRegistered()) return;

        Integer failedAttempts = account.getFailedLogInAttempts();
        failedAttempts++;
        account.setFailedLogInAttempts(failedAttempts);

        if (failedAttempts >= configLoader.getAllowedFailedAttempts()) {
            account.setActive(false);
            account.setFailedLogInAttempts(0);
            emailService.sendAccountBlockedDueToToManyLogInAttemptsEmail(account.getEmail(), account.getLocale());
        }
    }

    /**
     * Rejestruje udane logowanie na konto użytkownika poprzez wyzerowanie licznika nieudanych zalogowań.
     *
     * @param account Konto, dla którego należy wyzerować licznik nieudanych logowań
     */
    @PermitAll
    public void registerSuccessfulLogInAttempt(Account account) {
        if (!account.getActive() || !account.getRegistered()) return;
        account.setLastLogIn(LocalDateTime.now());
        account.setFailedLogInAttempts(0);
    }

    /**
     * Powiadamia administratora o zalogowaniu na jego konto poprzez wysłanie na adres email przypisany
     * do jego konta wiadomości zawierającej adres IP, z którego dokonane było logowanie
     *
     * @param account   konto administratora, na które doszło do zalogowania
     * @param ipAddress adres IP, z którego zostało wykonane logowanie
     */
    @PermitAll
    public void sendAdminAuthenticationWarningEmail(Account account, String ipAddress) {
        emailService.sendAdminAuthenticationWaringEmail(
                account.getEmail(),
                account.getLocale(),
                account.getLogin(),
                ipAddress
        );
    }

    @RolesAllowed(getAccountInfo)
    public ListResponseDto<TableAccountDto> findByNameSurname(
            String name,
            int page,
            int recordsPerPage,
            String orderBy,
            String order
    ) throws WrongParameterException {
        List<Account> list = accountFacade.findByNameSurname(name, page, recordsPerPage, orderBy, order);
        Long allRecords = accountFacade.getAccountListSizeNameSurname(name);
        return new ListResponseDto<>(
                page,
                (int) Math.ceil(allRecords.doubleValue() / recordsPerPage),
                recordsPerPage,
                allRecords,
                list.stream().map(TableAccountDto::new).collect(Collectors.toList())
        );
    }

    /**
     * Sprawdza, czy dany użytkownik ma uruchomione uwierzytelnianie dwuetapowe
     *
     * @param account użytkownik
     * @return true jeżeli użytkownik ma włączone uwierzytelnianie dwuetapowe
     * @return false jezeli użytkownik ma wyłaczone uwierzytelnianie dwuetapowe
     */
    @PermitAll
    public Boolean is2FAEnabledForUser(Account account) {
        return account.getTwoFAEnabled();
    }

    /**
     * Sprawdza, czy dany użytkownik miał już dane hasło ustawione w przeszłości
     *
     * @param newPassword nowe hasło do sprawdzenia
     * @param account     użytkownik zmieniający hasło
     * @return true jeżeli użytkownik nie miał ustawionego danego hasła
     */
    private boolean isPasswordUniqueForUser(String newPassword, Account account) {
        return account.getOldPasswordList().stream().noneMatch(op -> op.getPassword().equals(newPassword));
    }

    /**
     * Generuje oraz wysyła kod 2fa dla danego użytkownika na jego adres email
     *
     * @param account Konto użytkownika
     */
    @PermitAll
    public void send2faCode(Account account) {
        String totp = codeUtils.generateCode(account.getSecret());
        emailService.sendEmail2FA(
                account.getEmail(),
                account.getLocale(),
                account.getLogin(),
                totp
        );
    }

    /**
     * Zwraca historię zmian dla konta
     *
     * @param login          Login użytkownika, którego historia zmian konta ma być wyszukana
     * @param page           numer strony
     * @param recordsPerPage liczba rekordów na stronę
     * @param orderBy        kolumna po której następuje sortowanie
     * @param orderAsc       kolejność sortowania
     * @return Historia zmian konta
     * @throws BaseApplicationException jeżeli użytkownik o podanym loginie nie istnieje
     */
    @RolesAllowed({getOwnAccountInfo, getEnhancedAccountInfo})
    public List<AccountChangeLog> getAccountChangeLog(
            int page,
            int recordsPerPage,
            String login,
            String orderBy,
            Boolean orderAsc
    ) {
        return accountChangeLogFacade.findByLogin(login, orderBy, orderAsc, recordsPerPage, page);
    }


    @RolesAllowed({getOwnAccountInfo, getEnhancedAccountInfo})
    public Long getAccountLogListSize(String login) {
        return accountChangeLogFacade.getListSize(login);
    }


    /**
     * Wyłącza lub włącza dwustopniowe uwierzytelnianie dla użytkownika
     *
     * @param account Konto użytkownika
     */
    @PermitAll
    public void toggle2fa(Account account) throws BaseApplicationException {
        account.setTwoFAEnabled(!account.getTwoFAEnabled());
        accountFacade.update(account);
    }

    /**
     * Ustawia preferowany język przez użytkownika
     *
     * @param account Konto użytkownika
     * @param locale Język
     */
    @PermitAll
    public void changeAccountLocale(Account account, Locale locale) throws BaseApplicationException {
        account.setLocale(locale);
        accountFacade.update(account);
    }

}