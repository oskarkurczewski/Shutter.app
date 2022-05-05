package pl.lodz.p.it.ssbd2022.ssbd02.controllers;

import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.DataNotFoundException;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.PhotographerInfoDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.UserInfoDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.endpoint.PhotographerEndpoint;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/photographer")
public class PhotographerController {
    
    @Inject
    PhotographerEndpoint photographerEndpoint;

    @GET
    @Path("/{login}/info")
    @Produces(MediaType.APPLICATION_JSON)
    public PhotographerInfoDto getUserInfo(@NotNull @PathParam("login") String login) throws DataNotFoundException {
        return photographerEndpoint.getPhotographerInfo(login);
    }
}
