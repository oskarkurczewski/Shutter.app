package pl.lodz.p.it.ssbd2022.ssbd02.controllers;

import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.UserRegisterDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.UserStatusChangeDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.endpoint.UserEndpoint;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/account")
public class UserController {

    @Inject
    UserEndpoint userEndpoint;

    @PUT
    @Path("/{login}/status")
    @Consumes(MediaType.APPLICATION_JSON)
    public void changeUserStatus(
            @NotNull @PathParam("login") String login,
            @NotNull @Valid UserStatusChangeDto userStatusChangeDto
    ) {
        userEndpoint.blockUser(login, userStatusChangeDto.getActive());
    }

    /**
     * Punkt końcowy pozwalający na rejestrację użytkownika o poziomie dostępu klienta.
     * W przypadku powodzenia konto musi jeszcze zostać aktywowane w polu 'registered'.
     *
     * @param userRegisterDto Obiekt przedstawiające dane użytkownika do rejestracji
     * @return Odpowiedź HTTP
     * @throws BaseApplicationException Wyjątek aplikacyjny w przypadku niepowodzenia rejestracji użytkownika
     */
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerUser(@NotNull @Valid UserRegisterDto userRegisterDto) throws BaseApplicationException {
        userEndpoint.registerUser(userRegisterDto);
        return Response.status(Response.Status.CREATED).build();
    }

}
