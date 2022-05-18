package pl.lodz.p.it.ssbd2022.ssbd02.controllers;

import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.AvailabilityOverlapException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAvailabilityFoundException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoPhotographerFoundException;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.dto.AvailabilityDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.dto.CreateAvailabilityDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.dto.EditAvailabilityDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.endpoint.AvailabilityEndpoint;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("availability")
public class AvailabilityController {

    @Inject
    private AvailabilityEndpoint availabilityEndpoint;

    @POST
    @Path("/{login}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addAvailability(@NotNull @Valid CreateAvailabilityDto createAvailabilityDto)
            throws AvailabilityOverlapException {
        throw new UnsupportedOperationException();
    }

    @DELETE
    @Path("/{id}")
    public Response removeAvailability(@PathParam("id") Long availabilityId) throws NoAvailabilityFoundException {
        throw new UnsupportedOperationException();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editAvailability(
            @PathParam("id") Long availabilityId,
            @NotNull @Valid EditAvailabilityDto availabilityEdit
    ) throws NoAvailabilityFoundException {
        throw new UnsupportedOperationException();
    }

    @GET
    @Path("/{login}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<AvailabilityDto> listAvailabilities(
            @PathParam("login") String photographerLogin
    ) throws NoPhotographerFoundException {
        throw new UnsupportedOperationException();
    }
}
