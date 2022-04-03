package pl.lodz.p.it.ssbd2022.ssbd02.security;

import io.fusionauth.jwt.InvalidJWTException;
import io.fusionauth.jwt.Signer;
import io.fusionauth.jwt.Verifier;
import io.fusionauth.jwt.domain.JWT;
import io.fusionauth.jwt.hmac.HMACSigner;
import io.fusionauth.jwt.hmac.HMACVerifier;

import javax.security.enterprise.identitystore.CredentialValidationResult;
import java.time.ZonedDateTime;
import java.util.Set;

public class JWTHandler {

    private static final String SECRET = "90FAB1385C02FE80158890349649253C7F39121342FC09388427E8F49C4E7BF8";
    private static final int TIMEOUT = 10;

    private static final String ISSUER = "Shutter.app";

    public static String generateJWT(CredentialValidationResult validationResult) {

        String subject = validationResult.getCallerPrincipal().getName();
        Set<String> roles = validationResult.getCallerGroups();

        Signer signer = HMACSigner.newSHA512Signer(SECRET);

        JWT jwt = new JWT().setIssuer(ISSUER)
                .setIssuedAt(ZonedDateTime.now())
                .setSubject(subject)
                .addClaim("roles", String.join(",", roles))
                .setExpiration(ZonedDateTime.now().plusMinutes(TIMEOUT));

        String encodedJWT = JWT.getEncoder().encode(jwt, signer);

        return  encodedJWT;
    }

    public static Boolean validateJWT(String token) {

        try {
            Verifier verifier = HMACVerifier.newVerifier(SECRET);
            JWT jwt = JWT.getDecoder().decode(token, verifier);
            return jwt.isExpired();
        } catch (InvalidJWTException invalidJWTException) {
            return false;
        }
    }


}