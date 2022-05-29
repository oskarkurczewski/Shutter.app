package pl.lodz.p.it.ssbd2022.ssbd02.security;

import com.bastiaanjansen.otp.HMACAlgorithm;
import com.bastiaanjansen.otp.TOTP;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoConfigFileFound;
import pl.lodz.p.it.ssbd2022.ssbd02.util.ConfigLoader;
import pl.lodz.p.it.ssbd2022.ssbd02.util.LoggingInterceptor;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Stateless
@Interceptors({LoggingInterceptor.class})
public class OneTimeCodeUtils {
    @Inject
    private ConfigLoader configLoader;

    private TOTP createTotp(String secret) {
        return new TOTP.Builder(secret.getBytes(StandardCharsets.UTF_8))
                .withPasswordLength(6)
                .withAlgorithm(HMACAlgorithm.SHA512)
                .withPeriod(Duration.ofSeconds(configLoader.get2FaPeriod()))
                .build();
    }

    @PermitAll
    public String generateCode(String secret) {
        TOTP generator = createTotp(secret); return generator.now();
    }

    @PermitAll
    public boolean verifyCode(String secret, String code) {
        TOTP verifier = createTotp(secret);
        return verifier.verify(code);
    }
}
