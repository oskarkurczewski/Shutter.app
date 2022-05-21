package pl.lodz.p.it.ssbd2022.ssbd02.security;

import com.bastiaanjansen.otp.HMACAlgorithm;
import com.bastiaanjansen.otp.TOTP;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoConfigFileFound;
import pl.lodz.p.it.ssbd2022.ssbd02.util.ConfigLoader;
import pl.lodz.p.it.ssbd2022.ssbd02.util.LoggingInterceptor;

import javax.annotation.PostConstruct;
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

    private int period;

    public void setConfigLoader(ConfigLoader configLoader) {
        this.configLoader = configLoader;
    }

    @PostConstruct
    public void init() {
        try {
            period = Integer.parseInt(
                    configLoader
                    .loadProperties("config.2fa.properties")
                    .getProperty("2fa.period")
            );
        } catch (NoConfigFileFound e) {
            throw new RuntimeException(e);
        }
    }

    private TOTP createTotp(String secret) {
        return new TOTP.Builder(secret.getBytes(StandardCharsets.UTF_8))
                .withPasswordLength(6)
                .withAlgorithm(HMACAlgorithm.SHA512)
                .withPeriod(Duration.ofSeconds(period))
                .build();
    }

    public String generateCode(String secret) {
        TOTP generator = createTotp(secret);
        return generator.now();
    }
    public boolean verifyCode(String secret, String code) {
        TOTP verifier = createTotp(secret);
        return verifier.verify(code);
    }
}
