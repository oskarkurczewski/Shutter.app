package pl.lodz.p.it.ssbd2022.ssbd02.security.etag;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.ETagException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.ExceptionFactory;
import pl.lodz.p.it.ssbd2022.ssbd02.util.ConfigLoader;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Klasa z metodami do weryfikacji ETagu
 */
@Singleton
public class SignatureVerifier {

    private final static Logger LOGGER = Logger.getLogger(SignatureVerifier.class.getName());

    @Inject
    ConfigLoader configLoader;
    private String SECRET;

    @PostConstruct
    public void init() {
        SECRET = configLoader.getETagSecret();
    }

    public String calculateEntitySignature(SignableEntity signableEntity) throws ETagException {
        try {
            JWSSigner signer = new MACSigner(SECRET);
            JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.HS256), new Payload(signableEntity.getSignablePayload()));
            jwsObject.sign(signer);
            return jwsObject.serialize();
        } catch (JOSEException e) {
            LOGGER.log(Level.WARNING, "An exception occurred while creating ETag, caused by: " + e.getCause());
            throw ExceptionFactory.etagException();
        }
    }

    public boolean validateEntitySignature(String tagValue) throws ETagException {
        try {
            JWSObject jwsObject = JWSObject.parse(tagValue);
            JWSVerifier jwsVerifier = new MACVerifier(SECRET);

            return jwsObject.verify(jwsVerifier);
        } catch (JOSEException | ParseException e) {
            LOGGER.log(Level.WARNING, "An exception occurred while parsing ETag, caused by: " + e.getCause());
            throw ExceptionFactory.etagException();
        }
    }

    public boolean verifyEntityIntegrity(String tagValue, SignableEntity signableEntity) throws ETagException {
        try {
            final String ifMatchHeader = JWSObject.parse(tagValue).getPayload().toString();
            final String payload = signableEntity.getSignablePayload();
            return validateEntitySignature(tagValue) && ifMatchHeader.equals(payload);
        } catch (ParseException e) {
            throw ExceptionFactory.etagException();
        }
    }
}
