package pl.lodz.p.it.ssbd2022.ssbd02.util;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoAuthenticatedAccountFound;
import pl.lodz.p.it.ssbd2022.ssbd02.security.AuthenticationContext;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

/**
 * Szablonowa abstrakcyjna klasa interceptora pozwalającego na automatyczne aktualizowanie użytkowników, którzy
 * ostatnio dokonali zmiany na / lub utworzyli encję JPA
 */
public abstract class FacadeAccessInterceptorTemplate {

        @Inject
        private AuthenticationContext authenticationContext;

        protected abstract Account getCurrentlyAuthenticatedUserByLogin(String login) throws BaseApplicationException;

        @AroundInvoke
        public Object intercept(InvocationContext ctx) throws Exception {
            String methodName = ctx.getMethod().getName();
            if (!methodName.equals("persist") && !methodName.equals("update")) return ctx.proceed();

            Account account;
            try {

                account = getCurrentlyAuthenticatedUserByLogin(authenticationContext.getCurrentUsersLogin());
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
