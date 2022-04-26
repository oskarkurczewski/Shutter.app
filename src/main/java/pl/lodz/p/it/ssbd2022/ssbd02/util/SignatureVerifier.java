package pl.lodz.p.it.ssbd2022.ssbd02.util;


import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;

import javax.validation.Payload;
import java.text.ParseException;


public class SignatureVerifier {

    private final static String SECRET =
            "bx7sJvWXTEXltm2933p5i7r6quUHunFfUQWQdtaERysEx5600Wq3nQk4ChMiyc1z" +
                    "uGogM5oZ18DdkxK6VCu4qcvCiEKxiKcL5eMeVEBeo8oNsOhcre1tFHCd76APdtQc" +
                    "vuiFk3hU08hc4B1eMIkJpH83wM2oa3UWj422fHMstyKeKcst1L0OeP9sy7FPKlh6" +
                    "NiQyEvtOiuLVFnhu54K8PhjaB5djESwzS6wNaak5ZsFVvFCJbV1SK1OWWDFZkai1";


    public static String calculateEntitySignature(SignableEntity signableEntity) {
        try {
            JWSSigner signer = new MACSigner(SECRET);
            JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.HS256), new Payload(signableEntity.getSignablePayload()));
            jwsObject.sign(signer);
            return jwsObject.serialize();
        } catch (JOSEException e) {
            return "ETag failure";
        }
    }

    public static boolean validateEntitySignature(String tagValue) {
        try {
            JWSObject jwsObject = JWSObject.parse(tagValue);
            JWSVerifier jwsVerifier = new MACVerifier(SECRET);

            return jwsObject.verify(jwsVerifier);
        } catch (ParseException | JOSEException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean verifyEntityIntegrity(String tagValue, SignableEntity signableEntity) {
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
