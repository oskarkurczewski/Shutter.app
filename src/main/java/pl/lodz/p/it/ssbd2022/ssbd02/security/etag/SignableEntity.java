package pl.lodz.p.it.ssbd2022.ssbd02.security.etag;

/**
 * Klasa służąca do podpisywania obiektów w celu sprawdzenia ich wiarygodności
 */
public interface SignableEntity {
    String getSignablePayload();
}
