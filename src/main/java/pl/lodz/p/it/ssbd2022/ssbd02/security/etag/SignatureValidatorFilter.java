package pl.lodz.p.it.ssbd2022.ssbd02.security.etag;

import javax.ws.rs.NameBinding;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@NameBinding
@Retention(RetentionPolicy.RUNTIME)
public @interface SignatureValidatorFilter {
}
