package pl.lodz.p.it.ssbd2022.ssbd02.controllers;

import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.DataNotFoundException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.UnauthenticatedException;
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
public class PhotographerController {
    
    @Inject
    PhotographerEndpoint photographerEndpoint;

    /**
     * Punkt końcowy szukający fotografa
     *
     * @param login nazwa użytkownika fotografa
     * @throws DataNotFoundException W przypadku gdy fotograf o podanej nazwie użytkownika nie istnieje, 
     * gdy konto szukanego fotografa jest nieaktywne, niepotwierdzone lub profil nieaktywny i informacje prubuje uzyskać uzytkownik 
     * niebędący ani administratorem ani moderatorem
     * @throws UnauthenticatedException W przypadku gdy dane próbuje uzyskać niezalogowana osoba
     * @see PhotographerInfoDto
     */
    @GET
    @Path("/{login}/info")
    @Produces(MediaType.APPLICATION_JSON)
    public PhotographerInfoDto getUserInfo(@NotNull @PathParam("login") String login) throws DataNotFoundException, UnauthenticatedException {
        return photographerEndpoint.getPhotographerInfo(login);
    }
}
