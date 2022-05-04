package pl.lodz.p.it.ssbd2022.ssbd02.controllers;

import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAuthenticatedUserFound;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoUserFound;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.EditUserInfoDto;
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
        try {
            userEndpoint.blockUser(login, userStatusChangeDto.getActive());
        } catch (NoUserFound e) {
            // to bedzie trzeba kiedys bardziej uporzadkowac
            throw new WebApplicationException(e.getMessage(), Response.Status.BAD_REQUEST);
        }
    }

    /**
     * Pozwala zmienić informację aktualnie zalogowanego użytkownika w opraciu o aktualnie zalogowanego użytkownika.
     *
     * @param editUserInfoDto klasa zawierająca zmienione dane danego użytkownika
     */
    @PUT
    @Path("/editUserInfo")
    @Consumes(MediaType.APPLICATION_JSON)
    public void editUserInfo(
            @NotNull @Valid EditUserInfoDto editUserInfoDto
    ) {
        try {
            // Może zostać zwrócony obiekt użytkownika w przyszłości po edycji z userEndpoint
            userEndpoint.editUserInfo(editUserInfoDto);
        } catch (NoAuthenticatedUserFound e) {
            throw new WebApplicationException(e.getMessage(), Response.Status.BAD_REQUEST);
        }
    }
}
