package pl.lodz.p.it.ssbd2022.ssbd02.security.etag;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import pl.lodz.p.it.ssbd2022.ssbd02.util.ConfigLoader;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.text.ParseException;

/**
 * Klasa z metodami do weryfikacji ETagu
 */
@Singleton
public class SignatureVerifier {

    @Inject
    ConfigLoader configLoader;
    private String SECRET;

    @PostConstruct
    public void init() {
        SECRET = configLoader.getETagSecret();
    }

    public String calculateEntitySignature(SignableEntity signableEntity) {
        try {
            JWSSigner signer = new MACSigner(SECRET);
            JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.HS256), new Payload(signableEntity.getSignablePayload()));
            jwsObject.sign(signer);
            return jwsObject.serialize();
        } catch (JOSEException e) {
            return "ETag failure";
        }
    }

    public boolean validateEntitySignature(String tagValue) {
        try {
            JWSObject jwsObject = JWSObject.parse(tagValue);
            JWSVerifier jwsVerifier = new MACVerifier(SECRET);

            return jwsObject.verify(jwsVerifier);
        } catch (JOSEException | ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean verifyEntityIntegrity(String tagValue, SignableEntity signableEntity) {
        try {
            final String ifMatchHeader = JWSObject.parse(tagValue).getPayload().toString();
            final String payload = signableEntity.getSignablePayload();
            return validateEntitySignature(tagValue) && ifMatchHeader.equals(payload);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}
