package pl.lodz.p.it.ssbd2022.ssbd02.mow.service;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAccountFound;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.facade.AccountFacade;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AccountService {

    @Inject
    private AccountFacade accountFacade;

    /**
     * Odnajduje konto użytkownika o podanym loginie
     *
     * @param login Login użytkownika, którego konta ma być wyszukane
     * @throws BaseApplicationException bazowy wyjątek aplikacji
     */
    @PermitAll
    public Account findByLogin(String login) throws BaseApplicationException {
        return accountFacade.findByLogin(login);
    }
}
