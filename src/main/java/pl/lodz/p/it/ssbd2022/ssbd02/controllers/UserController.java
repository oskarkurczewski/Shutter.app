package pl.lodz.p.it.ssbd2022.ssbd02.controllers;

import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAuthenticatedUser;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.EditUserInfoDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.UserStatusChangeDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.UserUpdatePasswordDto;
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
        } catch (NoAuthenticatedUser e) {
            // to bedzie trzeba kiedys bardziej uporzadkowac
            throw new WebApplicationException(e.getMessage(), Response.Status.BAD_REQUEST);
        }
    }

    @PUT
    @Path("/{userId}/change-password")
    @Consumes(MediaType.APPLICATION_JSON)
    public void changeUserPasswordAsAdmin(
            @NotNull @PathParam("userId") Long userId,
            @NotNull @Valid UserUpdatePasswordDto password) {
        userEndpoint.updatePasswordAsAdmin(userId, password);
    }

    @PUT
    @Path("/editUserInfo")
    @Consumes(MediaType.APPLICATION_JSON)
    public void editUserInfo(
            @NotNull @Valid EditUserInfoDto editUserInfoDto
    ) {
        try {
            userEndpoint.editUserInfo(editUserInfoDto);
        } catch (NoAuthenticatedUser e) {
            throw new WebApplicationException(e.getMessage(), Response.Status.BAD_REQUEST);
        }
    }
}
