package pl.lodz.p.it.ssbd2022.ssbd02.mok.endpoint;

import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.DataNotFoundException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.UnauthenticatedException;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.PhotographerInfoDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.service.PhotographerService;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class PhotographerEndpoint {
    
    @Inject
    private PhotographerService photographerService;

    /**
     * Szuka fotografa
     *
     * @param login nazwa użytkownika fotografa
     * @throws DataNotFoundException W przypadku gdy fotograf o podanej nazwie użytkownika nie istnieje, 
     * gdy konto szukanego fotografa jest nieaktywne, niepotwierdzone lub profil nieaktywny i informacje prubuje uzyskać uzytkownik 
     * niebędący ani administratorem ani moderatorem
     * @throws UnauthenticatedException W przypadku gdy dane próbuje uzyskać niezalogowana osoba
     * @see PhotographerInfoDto
     */
    @RolesAllowed({"ADMINISTRATOR", "MODERATOR", "USER", "PHOTOGRAPHER"})
    public PhotographerInfoDto getPhotographerInfo(String login) throws DataNotFoundException, UnauthenticatedException {
        return photographerService.getPhotographerInfo(login);
    }
}
