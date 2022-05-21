package pl.lodz.p.it.ssbd2022.ssbd02.controllers;

import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.*;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.*;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAccountFound;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.endpoint.AccountEndpoint;
import pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint.Order;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

@Path("/account")
public class AccountController extends AbstractController {

    @Inject
    AccountEndpoint accountEndpoint;

    /**
     * Zmienia status użytkownika o danym loginie na zablokowany
     *
     * @param login login użytkownika dla którego ma zostać dokonana zmiana statusu
     */
    @PUT
    @Path("/{login}/block")
    @Consumes(MediaType.APPLICATION_JSON)
    public void blockAccount(
            @NotNull @PathParam("login") String login
    ) throws BaseApplicationException {
        repeat(() ->  accountEndpoint.blockAccount(login), accountEndpoint);
    }

    /**
     * Zmienia status użytkownika o danym loginie na odblokowany
     *
     * @param login login użytkownika, dla którego ma zostać dokonana zmiana statusu
     */
    @PUT
    @Path("/{login}/unblock")
    @Consumes(MediaType.APPLICATION_JSON)
    public void unblockAccount(
            @NotNull @PathParam("login") String login
    ) throws BaseApplicationException {
        repeat(() ->  accountEndpoint.unblockAccount(login), accountEndpoint);

    }

    @PUT
    @Path("/{login}/change-password")
    @Consumes(MediaType.APPLICATION_JSON)
    public void changeAccountPasswordAsAdmin(
            @NotNull @PathParam("login") String login,
            @NotNull @Valid AccountUpdatePasswordDto password) throws BaseApplicationException {
        repeat(() ->  accountEndpoint.updatePasswordAsAdmin(login, password), accountEndpoint);
    }

    @PUT
    @Path("/change-password")
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateOwnPassword(@NotNull @Valid AccountUpdatePasswordDto data) throws BaseApplicationException {
        repeat(() ->  accountEndpoint.updateOwnPassword(data), accountEndpoint);
    }

    /**
     * Wysyła link zawierający żeton resetu hasła na adres e-mail konta o podanym loginie
     *
     * @param login Login użytkownika, na którego email ma zostać wysłany link
     * @throws NoAccountFound Jeżeli konto nie istnieje w systemie lub jest niepotwierdzone/zablokowane
     */
    @POST
    @Path("{login}/request-reset")
    @Consumes(MediaType.APPLICATION_JSON)
    public void requestPasswordReset(@PathParam("login") String login) throws NoAccountFound {
        accountEndpoint.requestPasswordReset(login);
    }

    /**
     * Resetuje hasło dla użytkownika o podanym loginie
     *
     * @param login            Login użytkownika, dla którego ma zostać zresetowane hasło
     * @param resetPasswordDto Informacje wymagane do resetu hasła (żeton oraz nowe hasło)
     * @throws NoAccountFound           W przypadku gdy dany użytkownik nie istnieje
     * @throws InvalidTokenException    W przypadku gdy żeton jest nieprawidłowego typu
     * @throws ExpiredTokenException    W przypadku gdy żeton jest nieaktualny
     * @throws NoVerificationTokenFound W przypadku gdy żeton nie zostanie odnalenzniony w bazie danych
     */
    @POST
    @Path("{login}/password-reset")
    @Consumes(MediaType.APPLICATION_JSON)
    public void resetPassword(@PathParam("login") String login, @NotNull @Valid ResetPasswordDto resetPasswordDto)
            throws InvalidTokenException, NoAccountFound, NoVerificationTokenFound, ExpiredTokenException {
        accountEndpoint.resetPassword(login, resetPasswordDto);
    }


    /**
     * Wysyła link zawierający żeton zmiany adresu email
     *
     * @param email Nowy email użytkownika, na którego zostanie wysłany link weryfikacyjny
     * @throws NoAccountFound              Konto nie istnieje w systemie lub jest niepotwierdzone/zablokowane
     * @throws NoAuthenticatedAccountFound W przypadku gdy dane próbuje uzyskać niezalogowana osoba
     */
    @POST
    @Path("request-email-update")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response requestEmailUpdate(@NotNull @Valid RequestEmailUpdateDto email) throws NoAccountFound, NoAuthenticatedAccountFound {
        accountEndpoint.requestEmailUpdate(email);
        return Response.status(Response.Status.OK).build();
    }

    /**
     * Aktualizuje email danego użytkownika
     *
     * @param login          Login użytkownika, dla którego być zmieniony email
     * @param emailUpdateDto Informacje do zmiany emaila użytkownika
     * @throws NoAccountFound           W przypadku gdy dany użytkownik nie istnieje
     * @throws InvalidTokenException    Żeton jest nieprawidłowy
     * @throws NoVerificationTokenFound Nie udało się odnaleźć danego żetonu w systemie
     * @throws ExpiredTokenException    Żeton wygasł
     */
    @POST
    @Path("{login}/verify-email-update")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response verifyEmailUpdate(@PathParam("login") String login, @NotNull @Valid EmailUpdateDto emailUpdateDto) throws InvalidTokenException, ExpiredTokenException, NoVerificationTokenFound, NoAccountFound {
        accountEndpoint.updateEmail(login, emailUpdateDto);
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
        repeat(() ->  accountEndpoint.registerAccount(accountRegisterDto), accountEndpoint);
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
        repeat(() ->  accountEndpoint.confirmAccountRegistration(token), accountEndpoint);
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
        repeat(() ->  accountEndpoint.registerAccountByAdmin(accountRegisterAsAdminDto), accountEndpoint);

        return Response.status(Response.Status.CREATED).build();
    }

    /**
     * Punkt końcowy szukający użytkownika
     *
     * @param login nazwa użytkownika
     * @return obiekt DTO informacji o użytkowniku
     * @throws NoAccountFound              W przypadku gdy użytkownik o podanej nazwie nie istnieje lub
     *                                     gdy konto szukanego użytkownika jest nieaktywne, lub niepotwierdzone
     *                                     i informacje próbuje uzyskać użytkownik niebędący ani administratorem,
     *                                     ani moderatorem
     * @throws NoAuthenticatedAccountFound W przypadku gdy dane próbuje uzyskać niezalogowana osoba
     * @see AccountInfoDto
     */
    @GET
    @Path("/{login}/info")
    @Produces(MediaType.APPLICATION_JSON)
    public AccountInfoDto getUserInfo(@NotNull @PathParam("login") String login)
            throws BaseApplicationException {
        return repeat(() ->  accountEndpoint.getAccountInfo(login), accountEndpoint);
    }

    /**
     * Punkt końcowy zwracający dane o zalogowanym użytkowniku
     *
     * @return obiekt DTO informacji o użytkowniku
     * @throws NoAuthenticatedAccountFound W przypadku gdy dane próbuje uzyskać niezalogowana osoba
     * @see AccountInfoDto
     */
    @GET
    @Path("/info")
    @Produces(MediaType.APPLICATION_JSON)
    public AccountInfoDto getUserInfo() throws BaseApplicationException {
        return repeat(() ->  accountEndpoint.getOwnAccountInfo(), accountEndpoint);
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
        repeat(() ->  accountEndpoint.editAccountInfo(editAccountInfoDto), accountEndpoint);
    }

    /**
     * Pozwala zmienić informację użytkownika przez administratora przez podany login
     *
     * @param editAccountInfoDto klasa zawierająca zmienione dane danego użytkownika
     */
    @PUT
    @Path("/{login}/editAccountInfo")
    @Consumes(MediaType.APPLICATION_JSON)
    public void editAccountInfo(
            @NotNull @PathParam("login") String login,
            @NotNull @Valid EditAccountInfoDto editAccountInfoDto
    ) throws BaseApplicationException {
        // Może zostać zwrócony obiekt użytkownika w przyszłości po edycji z userEndpoint
        repeat(() ->  accountEndpoint.editAccountInfoAsAdmin(login, editAccountInfoDto), accountEndpoint);
    }

    /**
     * Punkt końcowy zwracający listę wszystkich użytkowników w zadanej kolejności spełniających warunki zapytania
     *
     * @param pageNo         numer strony do pobrania
     * @param recordsPerPage liczba rekordów na stronie
     * @param columnName     nazwa kolumny, po której nastąpi sortowanie
     * @param order          kolejność sortowania
     * @param login          nazwa użytkownika
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
    public ListResponseDto<String> getAccountList(
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
    ) throws WrongParameterException {
        return accountEndpoint.getAccountList(new AccountListRequestDto(
                        pageNo, recordsPerPage, columnName, order, login, email, name, surname, registered, active
                )
        );
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
        repeat(() ->  accountEndpoint.changeAccountAccessLevel(login, data), accountEndpoint);
        return Response.status(Response.Status.OK).build();
    }

}
