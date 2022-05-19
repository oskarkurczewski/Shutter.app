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
     * @param login                  login użytkownika dla którego ma zostać dokonana zmiana statusu
     * @param accountStatusChangeDto obiekt dto przechowujący status który ma zostać ustawiony
     */
    @PUT
    @Path("/{login}/block")
    @Consumes(MediaType.APPLICATION_JSON)
    public void blockAccount(
            @NotNull @PathParam("login") String login,
            @NotNull @Valid AccountStatusChangeDto accountStatusChangeDto
    ) throws NoAccountFound {
        accountEndpoint.blockAccount(login);
    }

    /**
     * Zmienia status użytkownika o danym loginie na odblokowany
     *
     * @param login                  login użytkownika, dla którego ma zostać dokonana zmiana statusu
     * @param accountStatusChangeDto obiekt dto przechowujący status, który ma zostać ustawiony
     */
    @PUT
    @Path("/{login}/unblock")
    @Consumes(MediaType.APPLICATION_JSON)
    public void unblockAccount(
            @NotNull @PathParam("login") String login,
            @NotNull @Valid AccountStatusChangeDto accountStatusChangeDto
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
     * @param data Obiekt przedstawiające dane zawierające poziom dostępu
     * @return Odpowiedź HTTP
     * @throws DataNotFoundException Wyjątek otrzymywany w przypadku próby dokonania operacji na niepoprawnej
     * nazwie poziomu dostępu lub próby ustawienia aktywnego/nieaktywnego już poziomu dostępu
     * @throws CannotChangeException Wyjątek otrzymywany w przypadku próby odebrania poziomu dostępu, którego użytkownik
     * nigdy nie posiadał
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
