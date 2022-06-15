package pl.lodz.p.it.ssbd2022.ssbd02.mow.facade;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAuthenticatedAccountFound;
import pl.lodz.p.it.ssbd2022.ssbd02.util.FacadeAccessInterceptorTemplate;

import javax.inject.Inject;

/**
 * Klasa interceptora pozwalające na automatyczne aktualizowanie użytkowników, którzy ostatnio dokonali zmiany
 * na / lub utworzyli encję JPA
 */
public class MowFacadeAccessInterceptor extends FacadeAccessInterceptorTemplate {

    @Inject
    private MowAccountFacade mowAccountFacade;

    @Override
    protected Account getCurrentlyAuthenticatedUserByLogin(String login) throws BaseApplicationException {
        return mowAccountFacade.findByLogin(login);
    }
}
