package pl.lodz.p.it.ssbd2022.ssbd02.controllers;

import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.*;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.*;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.endpoint.AccountEndpoint;
import pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint.Login;
import pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint.Order;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/account")
public class AccountController extends AbstractController {

    @Inject
    AccountEndpoint accountEndpoint;

    /**
     * Zmienia status użytkownika o danym loginie na zablokowany
     *
     * @param login Login użytkownika, dla którego ma zostać dokonana zmiana statusu
     */
    @PUT
    @Path("/{login}/block")
    @Consumes(MediaType.APPLICATION_JSON)
    public void blockAccount(
            @NotNull @PathParam("login") String login
    ) throws BaseApplicationException {
        repeat(() -> accountEndpoint.blockAccount(login), accountEndpoint);
    }

    /**
     * Zmienia status użytkownika o danym loginie na odblokowany
     *
     * @param login Login użytkownika, dla którego ma zostać dokonana zmiana statusu
     */
    @PUT
    @Path("/{login}/unblock")
    @Consumes(MediaType.APPLICATION_JSON)
    public void unblockAccount(
            @NotNull @PathParam("login") String login
    ) throws BaseApplicationException {
        repeat(() -> accountEndpoint.unblockAccount(login), accountEndpoint);
    }

    /**
     * Potwierdza aktywację własnego konta po długim czasie nieaktywności
     *
     * @param token Obiekt przedstawiający żeton weryfikacyjny użyty do aktywacji konta
     * @throws BaseApplicationException Występuje w przypadku gdy aktywacja konta się nie powiedzie
     */
    @PUT
    @Path("/unblock-own-account/{token}")
    public void confirmUnblockOwnAccount(@NotNull @PathParam("token") String token)
            throws BaseApplicationException {
        repeat(() -> accountEndpoint.confirmUnblockOwnAccount(token), accountEndpoint);
    }


    @PUT
    @Path("/{login}/change-password")
    @Consumes(MediaType.APPLICATION_JSON)
    public void changeAccountPasswordAsAdmin(
            @NotNull @PathParam("login") String login,
            @NotNull @Valid AccountUpdatePasswordDto password) throws BaseApplicationException {
        repeat(() -> accountEndpoint.updatePasswordAsAdmin(login, password), accountEndpoint);
    }

    @PUT
    @Path("/change-password")
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateOwnPassword(@NotNull @Valid AccountUpdatePasswordDto data) throws BaseApplicationException {
        repeat(() -> accountEndpoint.updateOwnPassword(data), accountEndpoint);
    }

    /**
     * Wysyła link zawierający żeton resetu hasła na adres e-mail konta o podanym loginie
     *
     * @param login Login użytkownika, na którego email ma zostać wysłany link
     * @throws NoAccountFound Konto o podanej nazwie nie istnieje lub jest niepotwierdzone/zablokowane
     */
    @POST
    @Path("{login}/request-reset")
    @Consumes(MediaType.APPLICATION_JSON)
    public void requestPasswordReset(@PathParam("login") String login,
                                     @NotNull RecaptchaTokenDto captcha) throws BaseApplicationException {
        repeat(() -> accountEndpoint.requestPasswordReset(login, captcha), accountEndpoint);
    }

    /**
     * Resetuje hasło dla użytkownika
     *
     * @param resetPasswordDto Informacje wymagane do resetu hasła (żeton oraz nowe hasło)
     * @throws InvalidTokenException    Żeton jest nieprawidłowego typu lub nieaktualny
     * @throws ExpiredTokenException    W przypadku gdy żeton jest nieaktualny
     * @throws NoVerificationTokenFound Żeton wygasł
     */
    @POST
    @Path("/password-reset")
    @Consumes(MediaType.APPLICATION_JSON)
    public void resetPassword(@NotNull @Valid ResetPasswordDto resetPasswordDto)
            throws BaseApplicationException {
        repeat(() -> accountEndpoint.resetPassword(resetPasswordDto), accountEndpoint);
    }


    /**
     * Wysyła link zawierający żeton zmiany adresu email
     *
     * @throws NoAccountFound              Konto nie istnieje w systemie lub jest niepotwierdzone/zablokowane
     * @throws NoAuthenticatedAccountFound W przypadku gdy dane próbuje uzyskać niezalogowana osoba
     */
    @POST
    @Path("request-email-update")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response requestEmailUpdate() throws BaseApplicationException {
        repeat(() -> accountEndpoint.requestEmailUpdate(), accountEndpoint);
        return Response.status(Response.Status.OK).build();
    }

    /**
     * Aktualizuje email użytkownika
     *
     * @param emailUpdateDto Informacje do zmiany emaila użytkownika
     * @throws InvalidTokenException    Żeton jest nieprawidłowego typu lub nieaktualny
     * @throws NoVerificationTokenFound Żeton nie zostanie odnaleziony w bazie
     * @throws ExpiredTokenException    Żeton wygasł
     */
    @POST
    @Path("/verify-email-update")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response verifyEmailUpdate(@NotNull @Valid EmailUpdateDto emailUpdateDto) throws BaseApplicationException {
        repeat(() -> accountEndpoint.updateEmail(emailUpdateDto), accountEndpoint);
        return Response.status(Response.Status.OK).build();
    }


    /**
     * Punkt końcowy pozwalający na rejestrację użytkownika o poziomie dostępu klienta.
     * W przypadku powodzenia konto musi jeszcze zostać aktywowane w polu 'registered'.
     *
     * @param accountRegisterDto Obiekt przedstawiające dane użytkownika do rejestracji
     * @return Odpowiedź HTTP
     * @throws IdenticalFieldException, Występuje w przypadku gdy rejestracja się nie powiedzie
     * @throws DatabaseException,       Występuje w przypadku gdy rejestracja się nie powiedzie
     */
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerAccount(@NotNull @Valid AccountRegisterDto accountRegisterDto) throws BaseApplicationException {
        repeat(() -> accountEndpoint.registerAccount(accountRegisterDto), accountEndpoint);
        return Response.status(Response.Status.CREATED).build();
    }

    /**
     * Punkt końcowy pozwalający na potwierdzenie rejestracji konta.
     *
     * @param token Obiekt przedstawiający żeton weryfikacyjny użyty do potwierdzenia rejestracji
     * @return Odpowiedź HTTP
     * @throws BaseApplicationException Wyjątek aplikacyjny w przypadku niepowodzenia potwierdzenia rejestracji
     */
    @POST
    @Path("/confirm/{token}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerAccount(@NotNull @Valid @PathParam("token") String token) throws BaseApplicationException {
        repeat(() -> accountEndpoint.confirmAccountRegistration(token), accountEndpoint);
        return Response.status(Response.Status.OK).build();
    }

    /**
     * Punkt końcowy pozwalający na rejestrację użytkownika o poziomie dostępu klienta, przez administratora.
     *
     * @param accountRegisterAsAdminDto Rozszerzony obiekt przedstawiające dane użytkownika do rejestracji
     * @return Odpowiedź HTTP
     * @throws IdenticalFieldException, Występuje w przypadku gdy rejestracja się nie powiedzie
     * @throws DatabaseException,       Występuje w przypadku gdy rejestracja się nie powiedzie
     */
    @POST
    @Path("/register-as-admin")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerAccountAsAdmin(@NotNull @Valid AccountRegisterAsAdminDto accountRegisterAsAdminDto)
            throws BaseApplicationException {
        repeat(() -> accountEndpoint.registerAccountByAdmin(accountRegisterAsAdminDto), accountEndpoint);
        return Response.status(Response.Status.CREATED).build();
    }

    /**
     * Punkt końcowy szukający użytkownika
     *
     * @param login Login użytkownika
     * @return obiekt DTO informacji o użytkowniku
     * @throws NoAccountFound Konto o podanej nazwie nie istnieje
     * @see BaseAccountInfoDto
     */
    @GET
    @Path("/{login}/detailed-info")
    @Produces(MediaType.APPLICATION_JSON)
    public DetailedAccountInfoDto getEnhancedAccountInfo(@NotNull @PathParam("login") String login)
            throws BaseApplicationException {
        return repeat(() -> accountEndpoint.getEnhancedAccountInfo(login), accountEndpoint);
    }

    /**
     * Punkt końcowy szukający użytkownika
     *
     * @param login Login użytkownika
     * @return obiekt DTO informacji o użytkowniku
     * @throws NoAccountFound Konto o podanej nazwie nie istnieje w systemie lub jest niepotwierdzone/zablokowane
     * @see BaseAccountInfoDto
     */
    @GET
    @Path("/{login}/info")
    @Produces(MediaType.APPLICATION_JSON)
    public BaseAccountInfoDto getAccountInfo(@NotNull @PathParam("login") String login)
            throws BaseApplicationException {
        return repeat(() -> accountEndpoint.getAccountInfo(login), accountEndpoint);
    }

    /**
     * Punkt końcowy zwracający dane o zalogowanym użytkowniku
     *
     * @return obiekt DTO informacji o użytkowniku
     * @throws NoAuthenticatedAccountFound W przypadku gdy dane próbuje uzyskać niezalogowana osoba
     * @see DetailedAccountInfoDto
     */
    @GET
    @Path("/info")
    @Produces(MediaType.APPLICATION_JSON)
    public DetailedAccountInfoDto getAccountInfo() throws BaseApplicationException {
        return repeat(() -> accountEndpoint.getOwnAccountInfo(), accountEndpoint);
    }

    /**
     * Pozwala zmienić informację aktualnie zalogowanego użytkownika na podstawie aktualnie zalogowanego użytkownika.
     *
     * @param editAccountInfoDto klasa zawierająca zmienione dane danego użytkownika
     */
    @PUT
    @Path("/editOwnAccountInfo")
    @Consumes(MediaType.APPLICATION_JSON)
    public void editOwnAccountInfo(
            @NotNull @Valid EditAccountInfoDto editAccountInfoDto
    ) throws BaseApplicationException {
        // Może zostać zwrócony obiekt użytkownika w przyszłości po edycji z userEndpoint
        repeat(() -> accountEndpoint.editAccountInfo(editAccountInfoDto), accountEndpoint);
    }

    /**
     * Pozwala zmienić informację użytkownika przez administratora przez podany login
     *
     * @param editAccountInfoAsAdminDto klasa zawierająca zmienione dane danego użytkownika
     */
    @PUT
    @Path("/{login}/editAccountInfo")
    @Consumes(MediaType.APPLICATION_JSON)
    public void editAccountInfo(
            @NotNull @PathParam("login") String login,
            @NotNull @Valid EditAccountInfoAsAdminDto editAccountInfoAsAdminDto
    ) throws BaseApplicationException {
        // Może zostać zwrócony obiekt użytkownika w przyszłości po edycji z userEndpoint
        repeat(() -> accountEndpoint.editAccountInfoAsAdmin(login, editAccountInfoAsAdminDto), accountEndpoint);
    }

    /**
     * Zwraca ostatnio ustawione w dla danego użytkownika preferencje sortowania oraz stronicowania list kont
     *
     * @throws BaseApplicationException kiedy preferencje dla danego użytkownika nie zostaną odnalezione
     */
    @GET
    @Path("/list/preferences")
    public AccountListPreferencesDto getAccountListPreferences()
            throws BaseApplicationException {
        return repeat(() -> accountEndpoint.getAccountListPreferences(), accountEndpoint);
    }

    /**
     * Punkt końcowy zwracający listę wszystkich użytkowników w zadanej kolejności spełniających warunki zapytania
     *
     * @param pageNo         numer strony do pobrania
     * @param recordsPerPage liczba rekordów na stronie
     * @param columnName     nazwa kolumny, po której nastąpi sortowanie
     * @param order          kolejność sortowania
     * @param login          Login użytkownika
     * @param email          email
     * @param name           imie
     * @param surname        nazwisko
     * @param registered     czy użytkownik zarejestrowany
     * @param active         czy konto aktywne
     * @return lista użytkowników
     * @throws WrongParameterException w przypadku gdy podano złą nazwę kolumny lub kolejność sortowania
     * @see ListResponseDto
     */
    @GET
    @Path("list")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ListResponseDto<TableAccountDto> getAccountList(
            @QueryParam("pageNo") @DefaultValue("1") int pageNo,
            @QueryParam("recordsPerPage") @NotNull int recordsPerPage,
            @QueryParam("columnName") @NotNull String columnName,
            @QueryParam("order") @Order @DefaultValue("asc") String order,
            @QueryParam("login") String login,
            @QueryParam("email") String email,
            @QueryParam("name") String name,
            @QueryParam("surname") String surname,
            @QueryParam("registered") Boolean registered,
            @QueryParam("active") Boolean active
    ) throws BaseApplicationException {
        return repeat(() -> accountEndpoint.getAccountList(
                pageNo, recordsPerPage, columnName, order, login, email, name, surname, registered, active
        ), accountEndpoint);
    }

    /**
     * Punkt końcowy pozwalający na dodanie poziomu uprawnień dla wskazanego użytkownika.
     *
     * @param data Obiekt przedstawiające dane zawierające poziom dostępu
     * @return Odpowiedź HTTP
     * @throws DataNotFoundException W przypadku próby podania niepoprawnej nazwie poziomu dostępu
     *                               lub próby ustawienia aktywnego/nieaktywnego już poziomu dostępu
     * @throws CannotChangeException W przypadku próby odebrania poziomu dostępu, którego użytkownik nigdy nie posiadał
     */
    @POST
    @Path("/{login}/accessLevel")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response assignAccountAccessLevel(
            @NotNull @PathParam("login") String login,
            @NotNull @Valid AccountAccessLevelChangeDto data
    ) throws BaseApplicationException {
        repeat(() -> accountEndpoint.changeAccountAccessLevel(login, data), accountEndpoint);
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("/list-name")
    @Produces(MediaType.APPLICATION_JSON)
    public ListResponseDto<String> findByNameSurname(
            @QueryParam("pageNo") @DefaultValue("1") int pageNo,
            @QueryParam("recordsPerPage") @NotNull int recordsPerPage,
            @QueryParam("columnName") @NotNull String columnName,
            @QueryParam("order") @Order @DefaultValue("asc") String order,
            @QueryParam("q") @NotNull String query
    ) throws BaseApplicationException {
        return repeat(() -> accountEndpoint.findByNameSurname(query, pageNo, recordsPerPage, columnName, order), accountEndpoint);
    }

    /**
     * Wysyła wymagany do zalogowania kod 2fa
     *
     * @param login login użytkownika, dla którego ma zostać utworzony kod 2fa
     * @return Odpowiedź HTTP
     * @throws BaseApplicationException W przypadku kiedy użytkownik o podanym loginie nie zostanie znaleziony
     *                                  lub wystąpi nieoczekiwany błąd
     */
    @POST
    @Path("/{login}/request-2fa-code")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response request2faCode(@NotNull @Login
                                   @PathParam("login") String login
    ) throws BaseApplicationException {
        repeat(() -> accountEndpoint.reguest2faCode(login), accountEndpoint);
        return Response.status(200).build();
    }

    /**
     * Punkt końcowy, który wyłącza lub włącza dwustopniowe uwierzytelnianie dla użytkownika
     *
     * @return Odpowiedź HTTP
     * @throws BaseApplicationException W przypadku kiedy użytkownik o podanym loginie nie zostanie znaleziony
     *                                  lub wystąpi nieoczekiwany błąd
     */
    @POST
    @Path("/toggle-2fa")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response toggle2fa() throws BaseApplicationException {
        repeat(() -> accountEndpoint.toggle2fa(), accountEndpoint);
        return Response.status(200).build();
    }

    /**
     * Punkt końcowy pozwalający użytkownikowi na zostanie fotografem.
     *
     * @return Odpowiedź HTTP
     * @throws DataNotFoundException W przypadku próby podania niepoprawnej nazwie poziomu dostępu
     *                               lub próby ustawienia aktywnego/nieaktywnego już poziomu dostępu
     * @throws CannotChangeException W przypadku próby odebrania poziomu dostępu, którego użytkownik nigdy nie posiadał
     */
    @POST
    @Path("/become-photographer")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response becomePhotographer() throws BaseApplicationException {
        repeat(() -> accountEndpoint.becomePhotographer(), accountEndpoint);
        return Response.status(Response.Status.OK).build();
    }

    /**
     * Punkt końcowy pozwalający na zaprzestanie bycia fotografem
     *
     * @return Odpowiedź HTTP
     * @throws DataNotFoundException       W przypadku próby podania niepoprawnej nazwie poziomu dostępu
     *                                     lub próby ustawienia aktywnego/nieaktywnego już poziomu dostępu
     * @throws CannotChangeException       W przypadku próby odebrania poziomu dostępu, którego użytkownik nigdy nie posiadał
     * @throws NoAuthenticatedAccountFound W przypadku nieznalezienia konta użytkownika w bazie danych
     *                                     na podstawie żetonu JWT
     * @throws NoPhotographerFound         W przypadku nieznalezienia konta fotografa
     */
    @POST
    @Path("/stop-being-photographer")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response stopBeingPhotographer() throws BaseApplicationException {
        repeat(() -> accountEndpoint.stopBeingPhotographer(), accountEndpoint);
        return Response.status(Response.Status.OK).build();
    }


}
