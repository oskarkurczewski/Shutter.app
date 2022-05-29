package pl.lodz.p.it.ssbd2022.ssbd02.security.etag;

import javax.json.bind.annotation.JsonbTransient;

/**
 * Klasa służąca do podpisywania obiektów w celu sprawdzenia ich wiarygodności
 */
public interface SignableEntity {
    @JsonbTransient
    String getSignablePayload();
}
