package pl.lodz.p.it.ssbd2022.ssbd02.mow.service;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.AccessLevelValue;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.DataNotFoundException;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.facade.AccountFacade;
import pl.lodz.p.it.ssbd2022.ssbd02.mow.facade.MowAccessLevelFacade;
import pl.lodz.p.it.ssbd2022.ssbd02.util.LoggingInterceptor;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;

@Stateless
@Interceptors({LoggingInterceptor.class})
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AccountService {

    @Inject
    private AccountFacade accountFacade;

    @Inject
    private MowAccessLevelFacade accessLevelFacade;

    /**
     * Odnajduje konto użytkownika o podanym loginie
     *
     * @param login login użytkownika do wyszukania
     * @return Konto użytkownika o podanym loginie
     * @throws BaseApplicationException gdy wystąpi problem z bazą danych
     */
    @PermitAll
    public Account findByLogin(String login) throws BaseApplicationException {
        return accountFacade.findByLogin(login);
    }

    /**
     * Odnajduje wybraną wartość poziomu dostępu na bazie jej nazwy
     *
     * @param name Nazwa poziomu dostępu
     * @return poziom dostępu o danej nazwie
     * @throws DataNotFoundException W momencie, gdy dany poziom dostępu nie zostanie odnaleziony
     */
    @PermitAll
    public AccessLevelValue findAccessLevelValueByName(String name) throws DataNotFoundException {
        return accessLevelFacade.getAccessLevelValue(name);
    }
}
