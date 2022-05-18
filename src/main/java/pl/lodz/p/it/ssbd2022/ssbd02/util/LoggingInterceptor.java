package pl.lodz.p.it.ssbd2022.ssbd02.util;

import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.UnauthorizedException;

import javax.ejb.EJBAccessException;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.security.enterprise.SecurityContext;
import java.security.Principal;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.logging.Logger;

/**
 * Klasa interceptora służąca do logowania wywołań metod w komponentach aplikacji
 */
public class LoggingInterceptor {
   private static final Logger LOGGER = Logger.getLogger(LoggingInterceptor.class.getName());
    @Inject
    private SecurityContext securityContext;

    @AroundInvoke
    public Object intercept(InvocationContext ctx) throws Exception {
        String caller = null;
        String methodName = null;
        String parameters = null;
        String result = null;
        Object returned;
        try {
            Principal callerPrincipal = securityContext.getCallerPrincipal();
            caller = callerPrincipal == null ? "Not authorized" : callerPrincipal.getName();
            methodName = ctx.getMethod().toGenericString();
            parameters = Arrays.toString(ctx.getParameters());
            returned = ctx.proceed();
            result = returned == null ? "No value was returned" : "Returned: " + returned;
            return returned;
        } catch (Exception ex) {
            result = "An exception occured: " + ex.getClass().toGenericString() + ", caused by: " + ex.getCause();
            if(ex instanceof EJBAccessException) {
                System.out.println("\n\n\n\nAAAA\n\n\n");
                throw new UnauthorizedException();
            }
            throw ex;
        } finally {
            String message = MessageFormat.format("Caller: {0}, called method: {1}, parameters: {2}, with result: {3}",
                    caller,
                    methodName,
                    parameters,
                    result);
            LOGGER.info(message);
        }
    }
}
