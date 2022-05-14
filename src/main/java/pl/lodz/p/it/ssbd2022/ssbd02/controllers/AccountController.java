package pl.lodz.p.it.ssbd2022.ssbd02.controllers;

import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.DatabaseException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.IdenticalFieldException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAccountFound;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAuthenticatedAccountFound;
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
     * Zmienia status użytkownika o danym loginie na podany
     *
     * @param login                  login użytkownika, dla którego ma zostać dokonana zmiana statusu
     * @param accountStatusChangeDto obiekt dto przechowujący status, który ma zostać ustawiony
     */
    @PUT
    @Path("/{login}/status")
    @Consumes(MediaType.APPLICATION_JSON)
    public void changeAccountStatus(
            @NotNull @PathParam("login") String login,
            @NotNull @Valid AccountStatusChangeDto accountStatusChangeDto
    ) throws NoAccountFound {
        accountEndpoint.changeAccountStatus(login, accountStatusChangeDto.getActive());
    }

    @PUT
    @Path("/{accountId}/change-password")
    @Consumes(MediaType.APPLICATION_JSON)
    public void changeAccountPasswordAsAdmin(
            @NotNull @PathParam("accountId") Long accountId,
            @NotNull @Valid AccountUpdatePasswordDto password) {
        accountEndpoint.updatePasswordAsAdmin(accountId, password);
    }

    @PUT
    @Path("/change-password")
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateOwnPassword(@NotNull @Valid AccountUpdatePasswordDto data) throws NoAuthenticatedAccountFound {
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
    public Response registerAccount(@NotNull @Valid AccountRegisterDto accountRegisterDto) throws IdenticalFieldException, DatabaseException {
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
    public Response registerAccountAsAdmin(@NotNull @Valid AccountRegisterAsAdminDto accountRegisterAsAdminDto) throws IdenticalFieldException, DatabaseException {
        accountEndpoint.registerAccountByAdmin(accountRegisterAsAdminDto);
        return Response.status(Response.Status.CREATED).build();
    }

    /**
     * Punkt końcowy szukający użytkownika
     *
     * @param login nazwa użytkownika
     * @return obiekt DTO informacji o użytkowniku
     * @throws NoAccountFound              W przypadku gdy użytkownik o podanej nazwie nie istnieje lub
     *                                     gdy konto szukanego użytkownika jest nieaktywne, lub niepotwierdzone i informacje próbuje uzyskać użytkownik
     *                                     niebędący ani administratorem, ani moderatorem
     * @throws NoAuthenticatedAccountFound W przypadku gdy dane próbuje uzyskać niezalogowana osoba
     * @see AccountInfoDto
     */
    @GET
    @Path("/{login}/info")
    @Produces(MediaType.APPLICATION_JSON)
    public AccountInfoDto getUserInfo(@NotNull @PathParam("login") String login) throws NoAuthenticatedAccountFound, NoAccountFound {
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
        return accountEndpoint.getYourAccountInfo();
    }

    /**
     * Pozwala zmienić informację aktualnie zalogowanego użytkownika na podstawie aktualnie zalogowanego użytkownika.
     *
     * @param editAccountInfoDto klasa zawierająca zmienione dane danego użytkownika
     */
    @PUT
    @Path("/editAccountInfo")
    @Consumes(MediaType.APPLICATION_JSON)
    public void editAccountInfo(
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

}
