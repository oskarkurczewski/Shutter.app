package pl.lodz.p.it.ssbd2022.ssbd02.security;

import io.fusionauth.jwt.InvalidJWTException;
import io.fusionauth.jwt.JWTExpiredException;
import io.fusionauth.jwt.Signer;
import io.fusionauth.jwt.Verifier;
import io.fusionauth.jwt.domain.JWT;
import io.fusionauth.jwt.hmac.HMACSigner;
import io.fusionauth.jwt.hmac.HMACVerifier;

import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.servlet.http.HttpServletRequest;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

/**
 * Klasa służąca tworzeniu żetonów JWT oraz ich walidacji
 */
public class JWTHandler {

    public final static String AUTH_HEADER = "Authorization";
    public final static String BEARER = "Bearer ";

    private static final String SECRET = "90FAB1385C02FE80158890349649253C7F39121342FC09388427E8F49C4E7BF8";
    private static final int TIMEOUT = 10;
    private static final String ISSUER = "Shutter.app";

    /**
     * Metoda tworząca podpisany żeton JWT algorytmem HS512 przechowujący poziomy dostępu użytkownika, czas wygaśnięcia żetonu,
     * identyfikator użytkownika, czas podpisania, identyfikator jednostki wystawiającej
     *
     * @param validationResult Obiekt klasy CredentialValidationResult zawierający rezultat walidacji poświadczeń użytkownika
     * @return String przedstawiający utworzony żeton JWT
     */
    public static String generateJWT(CredentialValidationResult validationResult) {
        String subject = validationResult.getCallerPrincipal().getName();
        Set<String> roles = validationResult.getCallerGroups();

        Signer signer = HMACSigner.newSHA512Signer(SECRET);

        JWT jwt = new JWT().setIssuer(ISSUER)
                .setIssuedAt(ZonedDateTime.now())
                .setSubject(subject)
                .addClaim("roles", String.join(",", roles))
                .setExpiration(ZonedDateTime.now().plusMinutes(TIMEOUT));

        return JWT.getEncoder().encode(jwt, signer);
    }

    /**
     * Uzyskuje żeton JWT z żądania
     *
     * @param request Obiekt żądania
     * @return żeton JWT uzyskany z obiektu żądania
     */
    public static JWT getJwtFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTH_HEADER);
        return getJwtFromAuthHeader(authHeader);
    }

    /**
     * Uzyskuje żeton z nagłówka "Authorization"
     *
     * @param authHeader Nagłówek "Authorization"
     * @return żeton JWT uzyskany z nagłówka
     */
    public static JWT getJwtFromAuthHeader(String authHeader) {
        if (authHeader == null || !authHeader.startsWith(BEARER)) return null;
        return JWTHandler.decodeJwt(authHeader.substring(BEARER.length()));
    }

    /**
     * Metoda dekodująca żeton JWT z ciągu znaków, weryfikująca jego sygnaturę
     * oraz czy dalej jest ważny.
     *
     * @param token żeton JWT przesłany przez użytkownika
     * @return Zwraca zdekodowany żeton, jeżeli żeton jest błędny lub nieważny,
     * metoda zwraca null
     */
    public static JWT decodeJwt(String token) {
        try {
            Verifier verifier = HMACVerifier.newVerifier(SECRET);
            JWT jwt = JWT.getDecoder().decode(token, verifier);
            if (jwt.isExpired()) return null;
            return jwt;
        } catch (InvalidJWTException | JWTExpiredException exception) {
            return null;
        }
    }

    /**
     * Odświeża podany żeton JWT
     * @param oldToken Żeton, który ma zostać odświeżony
     * @return Odświeżony żeton JWT
     */
    public static String refresh(JWT oldToken, List<String> newGroups) {
        Signer signer = HMACSigner.newSHA512Signer(SECRET);

        JWT refreshed = new JWT().setIssuer(ISSUER)
                .setIssuedAt(ZonedDateTime.now())
                .setSubject(oldToken.subject)
                .addClaim("roles", String.join(",", newGroups))
                .setExpiration(ZonedDateTime.now().plusMinutes(TIMEOUT));

        return JWT.getEncoder().encode(refreshed, signer);
    }

}