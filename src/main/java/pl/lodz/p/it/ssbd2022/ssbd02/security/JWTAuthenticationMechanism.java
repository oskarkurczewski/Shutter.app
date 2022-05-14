package pl.lodz.p.it.ssbd2022.ssbd02.security;

import io.fusionauth.jwt.domain.JWT;

import javax.enterprise.context.RequestScoped;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Klasa posiadająca odpowiedzialność uwierzytelniania oraz autoryzowania
 * użytkownika na podstawie żetonu JWT przesłanego w żądaniu.
 */
@RequestScoped
public class JWTAuthenticationMechanism implements HttpAuthenticationMechanism {

    public final static String AUTH_HEADER = "Authorization";
    public final static String BEARER = "Bearer";


    /**
     * Pobiera żeton JWT z nagłówka "Authorization" żądania. Jeżeli żeton
     * JWT jest prawidłowy, to przekazuje kontenerowi login użytkownika oraz
     * jego role. Jeżeli nagłówek żądania jest niepoprawny, to sprawdza, czy
     * metoda jest chroniona, jeżeli nie, to nie sprawdza tokenu, jeżeli tak,
     * to operacja nie powodzi się
     *
     * @param httpServletRequest  żądanie
     * @param httpServletResponse odpowiedź
     * @param httpMessageContext  kontekst
     * @return status operacji uwierzytelniania
     */
    @Override
    public AuthenticationStatus validateRequest(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            HttpMessageContext httpMessageContext
    ) {
        JWT jwt = getJwtFromRequest(httpServletRequest);
        if (jwt != null) {
            Set<String> roles = new HashSet<>(Arrays.asList(jwt.getString("roles").split(",")));
            return httpMessageContext.notifyContainerAboutLogin(
                    jwt.getString("sub"), roles
            );
        } else if (!httpMessageContext.isProtected()) {
            return httpMessageContext.doNothing();
        }

        return httpMessageContext.responseNotFound();
    }


    private JWT getJwtFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTH_HEADER);
        if (authHeader == null || !authHeader.startsWith(BEARER + " ")) return null;

        return JWTHandler.decodeJwt(authHeader.substring(BEARER.length() + 1));
    }
}
