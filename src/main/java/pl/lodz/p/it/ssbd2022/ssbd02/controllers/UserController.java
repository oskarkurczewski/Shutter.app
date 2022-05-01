package pl.lodz.p.it.ssbd2022.ssbd02.controllers;

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

    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public void registerUser(@NotNull @Valid UserRegisterDto userRegisterDto) {
        userEndpoint.registerUser(userRegisterDto);
    }

}
