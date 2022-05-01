package pl.lodz.p.it.ssbd2022.ssbd02.mok.service;


import pl.lodz.p.it.ssbd2022.ssbd02.entity.PhotographerInfo;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.dto.PhotographerInfoDto;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.facade.AuthenticationFacade;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class PhotographerService {
    
    @Inject
    private AuthenticationFacade userFacade;

    @RolesAllowed({"ADMINISTRATOR", "MODERATOR", "USER", "PHOTOGRAPHER"})
    public PhotographerInfoDto getPhotographerInfo(String login){
        PhotographerInfo photographerInfo = userFacade.findPhotographerByLogin(login);
        return new PhotographerInfoDto(photographerInfo);
    }
}
