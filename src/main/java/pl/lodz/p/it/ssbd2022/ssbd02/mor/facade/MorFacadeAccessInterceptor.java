package pl.lodz.p.it.ssbd2022.ssbd02.mor.facade;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.util.FacadeAccessInterceptorTemplate;

import javax.inject.Inject;

/**
 * Klasa interceptora pozwalające na automatyczne aktualizowanie użytkowników, którzy ostatnio dokonali zmiany
 * na / lub utworzyli encję JPA
 */
public class MorFacadeAccessInterceptor extends FacadeAccessInterceptorTemplate {

    @Inject
    private MorAccountFacade morAccountFacade;

    @Override
    protected Account getCurrentlyAuthenticatedUserByLogin(String login) throws BaseApplicationException {
        return morAccountFacade.findByLogin(login);
    }
}
