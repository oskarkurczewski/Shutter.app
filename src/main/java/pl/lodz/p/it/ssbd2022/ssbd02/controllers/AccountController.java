package pl.lodz.p.it.ssbd2022.ssbd02.controllers;

import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.CannotChangeException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.DataNotFoundException;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.*;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAuthenticatedUserFound;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAccountFound;
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
     * @param login login użytkownika dla którego ma zostać dokonana zmiana statusu
     * @param accountStatusChangeDto obiekt dto przechowujący status który ma zostać ustawiony
     */
    @PUT
    @Path("/{login}/status")
    @Consumes(MediaType.APPLICATION_JSON)
    public void changeAccountStatus (
            @NotNull @PathParam("login") String login,
            @NotNull @Valid AccountStatusChangeDto accountStatusChangeDto
    ) {
        try {
            accountEndpoint.changeAccountStatus(login, accountStatusChangeDto.getActive());
        } catch (NoAccountFound e) {
            // to bedzie trzeba kiedys bardziej uporzadkowac
            throw new WebApplicationException(e.getMessage(), Response.Status.NOT_FOUND);
        }
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
    public void updateOwnPassword(@NotNull @Valid AccountUpdatePasswordDto data) {
        try {
            accountEndpoint.updateOwnPassword(data);
        } catch (NoAuthenticatedUserFound e) {
            throw new WebApplicationException(e.getMessage(), Response.Status.NOT_FOUND);
        }
    }

    /**
     * Punkt końcowy pozwalający na rejestrację użytkownika o poziomie dostępu klienta.
     * W przypadku powodzenia konto musi jeszcze zostać aktywowane w polu 'registered'.
     *
     * @param accountRegisterDto Obiekt przedstawiające dane użytkownika do rejestracji
     * @return Odpowiedź HTTP
     * @throws BaseApplicationException Wyjątek aplikacyjny w przypadku niepowodzenia rejestracji użytkownika
     */
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerAccount(@NotNull @Valid AccountRegisterDto accountRegisterDto) throws BaseApplicationException {
        accountEndpoint.registerAccount(accountRegisterDto);
        return Response.status(Response.Status.CREATED).build();
    }

    /**
     * Pozwala zmienić informację aktualnie zalogowanego użytkownika w opraciu o aktualnie zalogowanego użytkownika.
     *
     * @param editAccountInfoDto klasa zawierająca zmienione dane danego użytkownika
     */
    @PUT
    @Path("/editAccountInfo")
    @Consumes(MediaType.APPLICATION_JSON)
    public void editAccountInfo(
            @NotNull @Valid EditAccountInfoDto editAccountInfoDto
    ) {
        try {
            // Może zostać zwrócony obiekt użytkownika w przyszłości po edycji z userEndpoint
            accountEndpoint.editAccountInfo(editAccountInfoDto);
        } catch (NoAuthenticatedUserFound e) {
            throw new WebApplicationException(e.getMessage(), Response.Status.NOT_FOUND);
        }
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
    @Path("/{accountId}/accessLevel")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response assignAccountAccessLevel(
            @NotNull @PathParam("accountId") Long accountId,
            @NotNull @Valid AccountAccessLevelChangeDto data
    ) throws CannotChangeException, DataNotFoundException {
        accountEndpoint.changeAccountAccessLevel(accountId, data);
        return Response.status(Response.Status.OK).build();
    }

}
