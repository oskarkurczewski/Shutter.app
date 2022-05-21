package pl.lodz.p.it.ssbd2022.ssbd02.controllers;

import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.*;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.AccountInfoDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.*;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.endpoint.AccountEndpoint;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/account")
public class AccountController {

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
    ) throws NoAccountFound {
        accountEndpoint.blockAccount(login);
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
    ) throws NoAccountFound {
        accountEndpoint.unblockAccount(login);
    }

    @PUT
    @Path("/{login}/change-password")
    @Consumes(MediaType.APPLICATION_JSON)
    public void changeAccountPasswordAsAdmin(
            @NotNull @PathParam("login") String login,
            @NotNull @Valid AccountUpdatePasswordDto password) throws NoAccountFound {
        accountEndpoint.updatePasswordAsAdmin(login, password);
    }

    @PUT
    @Path("/change-password")
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateOwnPassword(@NotNull @Valid AccountUpdatePasswordDto data) throws NoAuthenticatedAccountFound, PasswordMismatchException {
        accountEndpoint.updateOwnPassword(data);
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
    public Response registerAccount(@NotNull @Valid AccountRegisterDto accountRegisterDto)
            throws IdenticalFieldException, DatabaseException, DataNotFoundException {
        accountEndpoint.registerAccount(accountRegisterDto);
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
        accountEndpoint.confirmAccountRegistration(token);
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
            throws IdenticalFieldException, DatabaseException, DataNotFoundException {
        accountEndpoint.registerAccountByAdmin(accountRegisterAsAdminDto);
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
            throws NoAuthenticatedAccountFound, NoAccountFound {
        return accountEndpoint.getAccountInfo(login);
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
    public AccountInfoDto getUserInfo() throws NoAuthenticatedAccountFound {
        return accountEndpoint.getOwnAccountInfo();
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
    ) throws NoAuthenticatedAccountFound {
        // Może zostać zwrócony obiekt użytkownika w przyszłości po edycji z userEndpoint
        accountEndpoint.editAccountInfo(editAccountInfoDto);
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
    ) throws NoAccountFound {
        // Może zostać zwrócony obiekt użytkownika w przyszłości po edycji z userEndpoint
        accountEndpoint.editAccountInfoAsAdmin(login, editAccountInfoDto);
    }

    /**
     * Punkt końcowy pozwalający na dodanie poziomu uprawnień dla wskazanego użytkownika.
     *
     * @param data                      Obiekt przedstawiające dane zawierające poziom dostępu
     * @return Odpowiedź HTTP
     * @throws DataNotFoundException    W przypadku próby podania niepoprawnej nazwie poziomu dostępu
     * lub próby ustawienia aktywnego/nieaktywnego już poziomu dostępu
     * @throws CannotChangeException    W przypadku próby odebrania poziomu dostępu, którego użytkownik nigdy nie posiadał
     */
    @POST
    @Path("/{login}/accessLevel")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response assignAccountAccessLevel(
            @NotNull @PathParam("login") String login,
            @NotNull @Valid AccountAccessLevelChangeDto data
    ) throws CannotChangeException, DataNotFoundException, NoAccountFound {
        accountEndpoint.changeAccountAccessLevel(login, data);
        return Response.status(Response.Status.OK).build();
    }

}
