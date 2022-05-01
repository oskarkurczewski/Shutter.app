package pl.lodz.p.it.ssbd2022.ssbd02.controllers;

import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.UserInfoDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.UserStatusChangeDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.endpoint.UserEndpoint;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

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

    @GET
    @Path("/{login}/info")
    @Produces(MediaType.APPLICATION_JSON)
    public UserInfoDto getUserInfo(@NotNull @PathParam("login") String login) {
        return userEndpoint.getUserInfo(login);
    }
}
