package pl.lodz.p.it.ssbd2022.ssbd02.util;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAuthenticatedAccountFound;
import pl.lodz.p.it.ssbd2022.ssbd02.security.AuthenticationContext;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

/**
 * Klasa interceptora pozwalające na automatyczne aktualizowanie użytkowników, którzy ostatnio dokonali zmiany
 * na / lub utworzyli encję JPA
 */
public class FacadeAccessInterceptor {
    @Inject
    private AuthenticationContext authenticationContext;

    @AroundInvoke
    public Object intercept(InvocationContext ctx) throws Exception {
        if (!ctx.getMethod().getDeclaringClass().getPackageName().contains("facade")) return ctx.proceed();
        String methodName = ctx.getMethod().getName();
        try {
            if (methodName.equals("persist")) {
                ManagedEntity entity = (ManagedEntity) ctx.getParameters()[0];
                Account modifier = authenticationContext.getCurrentUsersAccount();
                entity.setCreatedBy(modifier);
            }
            if (methodName.equals("update")) {
                ManagedEntity entity = (ManagedEntity) ctx.getParameters()[0];
                Account modifier = authenticationContext.getCurrentUsersAccount();
                entity.setModifiedBy(modifier);
            }
        } catch (NoAuthenticatedAccountFound e) {}
        return ctx.proceed();
    }
}
