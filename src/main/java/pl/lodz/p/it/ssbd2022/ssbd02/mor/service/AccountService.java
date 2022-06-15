package pl.lodz.p.it.ssbd2022.ssbd02.mor.service;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.mor.facade.MorAccountFacade;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import static pl.lodz.p.it.ssbd2022.ssbd02.security.Roles.reservePhotographer;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AccountService {

    @Inject
    MorAccountFacade morAccountFacade;

    @RolesAllowed(reservePhotographer)
    public Account findByLogin(String login) throws BaseApplicationException {
        return morAccountFacade.findByLogin(login);
    }
}
