package pl.lodz.p.it.ssbd2022.ssbd02.controllers;

import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAuthenticatedAccountFound;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoPhotographerFound;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.PhotographerInfoDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.endpoint.PhotographerEndpoint;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/photographer")
public class PhotographerController extends AbstractController {

    @Inject
    PhotographerEndpoint photographerEndpoint;

    /**
     * Punkt końcowy szukający fotografa
     *
     * @param login nazwa użytkownika fotografa
     * @throws NoPhotographerFound         W przypadku gdy fotograf o podanej nazwie użytkownika nie istnieje,
     *                                     gdy konto szukanego fotografa jest nieaktywne, niepotwierdzone lub profil nieaktywny i informacje próbuje uzyskać użytkownik
     *                                     niebędący ani administratorem, ani moderatorem
     * @throws NoAuthenticatedAccountFound W przypadku gdy dane próbuje uzyskać niezalogowana osoba
     * @see PhotographerInfoDto
     */
    @GET
    @Path("/{login}/info")
    @Produces(MediaType.APPLICATION_JSON)
    public PhotographerInfoDto getUserInfo(@NotNull @PathParam("login") String login) throws BaseApplicationException {
        return repeat(() ->  photographerEndpoint.getPhotographerInfo(login), photographerEndpoint);
    }

    /**
     * Punkt końcowy zwracający informacje o zalogowanym fotografie
     *
     * @throws NoPhotographerFound         W przypadku gdy profil fotografa dla użytkownika nie istnieje
     * @throws NoAuthenticatedAccountFound W przypadku gdy dane próbuje uzyskać niezalogowana osoba
     * @see PhotographerInfoDto
     */
    @GET
    @Path("/info")
    @Produces(MediaType.APPLICATION_JSON)
    public PhotographerInfoDto getUserInfo() throws BaseApplicationException {
        return repeat(() ->  photographerEndpoint.getYourPhotographerInfo(), photographerEndpoint);
    }
}
