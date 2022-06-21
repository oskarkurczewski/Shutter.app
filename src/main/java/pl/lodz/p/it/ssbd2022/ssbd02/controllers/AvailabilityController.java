package pl.lodz.p.it.ssbd2022.ssbd02.controllers;

import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoPhotographerFoundException;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.dto.AvailabilityDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.dto.EditAvailabilityDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.endpoint.AvailabilityEndpoint;
import pl.lodz.p.it.ssbd2022.ssbd02.validation.constraint.Login;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("availability")
public class AvailabilityController extends AbstractController {

    @Inject
    private AvailabilityEndpoint availabilityEndpoint;

    /**
     * Nadpisuje listę godzin dostępności fotografa
     *
     * @param availabilities nowa lista dostępności fotografa
     * @return Response
     * @throws BaseApplicationException kiedy operacja się nie powiedzie
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response editAvailability(
            @NotNull @Valid List<EditAvailabilityDto> availabilities
    ) throws BaseApplicationException {
        repeat(() -> availabilityEndpoint.editAvailability(availabilities), availabilityEndpoint);
        return Response.ok().build();
    }

    /**
     * Zwraca listę godzin dostępności dla podanego fotografa
     *
     * @param login login fotografa
     * @return AvailabilityDto lista godzin dostępności
     * @throws NoPhotographerFoundException nie znaleziono fotografa o podanym loginie
     */
    @GET
    @Path("/{login}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listAvailabilities(
            @NotNull @Login @PathParam("login") String login
    ) throws BaseApplicationException {
        List<AvailabilityDto> availabilities = repeat(() -> availabilityEndpoint.listAvailabilities(login), availabilityEndpoint);
        return Response.ok().entity(availabilities).build();
    }
}
