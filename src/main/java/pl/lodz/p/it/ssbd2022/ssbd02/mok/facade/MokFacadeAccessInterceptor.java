package pl.lodz.p.it.ssbd2022.ssbd02.mok.facade;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAuthenticatedAccountFound;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.service.AccountService;
import pl.lodz.p.it.ssbd2022.ssbd02.security.AuthenticationContext;
import pl.lodz.p.it.ssbd2022.ssbd02.util.ManagedEntity;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
/**
 * Klasa interceptora pozwalające na automatyczne aktualizowanie użytkowników, którzy ostatnio dokonali zmiany
 * na / lub utworzyli encję JPA
 */
public class MokFacadeAccessInterceptor {
    @Inject
    private AuthenticationContext authenticationContext;

    @Inject
    private AccountService accountService;

    @AroundInvoke
    public Object intercept(InvocationContext ctx) throws Exception {
        String methodName = ctx.getMethod().getName();
        if (!methodName.equals("persist") && !methodName.equals("update")) return ctx.proceed();

        Account account;
        try {
            account = accountService.findByLogin(authenticationContext.getCurrentUsersLogin());
        } catch (NoAuthenticatedAccountFound e) {
            return ctx.proceed();
        }

        ManagedEntity entity = (ManagedEntity) ctx.getParameters()[0];
        if (methodName.equals("persist")) {
            entity.setCreatedBy(account);
        } else {
            entity.setModifiedBy(account);
        }
        return ctx.proceed();
    }
}
