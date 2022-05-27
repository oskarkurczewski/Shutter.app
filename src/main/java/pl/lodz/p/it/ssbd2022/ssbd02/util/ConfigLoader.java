package pl.lodz.p.it.ssbd2022.ssbd02.util;
import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Klasa służąca do wczytywania plików konfiguracyjnych
 */
@PermitAll
@Startup
@Singleton
@Interceptors({LoggingInterceptor.class})
public class ConfigLoader {
    private static final String PROPERTIES_TOKEN_FILE = "config.token.properties";
    private static final String PROPERTIES_2FA_FILE = "config.2fa.properties";
    private static final String PROPERTIES_TIMEOUT_FILE = "config.timeout.properties";
    private static final String PROPERTIES_TRANSACTION_FILE = "config.transaction.properties";
    private static final String PROPERTIES_EMAIL_FILE = "config.email.properties";

    private static final String PROPERTIES_RECAPTCHA_FILE = "config.recaptcha.properties";
    private static final String REGISTRATION_CONFIRMATION_TOKEN_LIFETIME = "registration.confirmation.token.lifespan";
    private static final String EMAIL_RESET_TOKEN_LIFETIME = "email.reset.token.lifespan";
    private static final String UNBLOCK_OWN_ACCOUNT_TOKEN_LIFESPAN = "unblock.own.account.token.lifespan";
    private static final String PASSWORD_RESET_TOKEN_LIFETIME = "password.reset.token.lifespan";
    private static final String FORCED_PASSWORD_RESET_TOKEN_LIFETIME = "forced.password.reset.token.lifespan";
    private static final String UNBLOCK_OWN_ACCOUNT_TOKEN_LIFETIME = "unblock.own.account.token.lifespan";
    private static final String PERIOD_2FA = "2fa.period";
    private static final String EMAIL_API_KEY = "api.key";
    private static final String EMAIL_SENDER_ADDRESS = "email.sender.email";
    private static final String EMAIL_SENDER_NAME = "email.sender.name";
    private static final String EMAIL_APP_NAME = "app.name";
    private static final String EMAIL_APP_URL = "app.url";
    private static final String BLOCK_CHECK_TIMEOUT = "block.check-timeout";
    private static final String BLOCK_TIMEOUT = "block.timeout";
    private static final String TRANSACTION_REPETITION_LIMIT = "transaction.repetition.limit";

    private static final String RECAPTCHA_API_KEY = "recaptcha.api.key";

    private Properties propertiesToken;
    private Properties properties2Fa;
    private Properties propertiesEmail;
    private Properties propertiesTimeout;
    private Properties propertiesTransaction;

    private Properties propertiesRecaptcha;

    public ConfigLoader() {
    }

    @PostConstruct
    private void init() {
        propertiesToken = loadProperties(PROPERTIES_TOKEN_FILE);
        properties2Fa = loadProperties(PROPERTIES_2FA_FILE);
        propertiesEmail = loadProperties(PROPERTIES_EMAIL_FILE);
        propertiesTimeout = loadProperties(PROPERTIES_TIMEOUT_FILE);
        propertiesTransaction = loadProperties(PROPERTIES_TRANSACTION_FILE);
        propertiesRecaptcha = loadProperties(PROPERTIES_RECAPTCHA_FILE);
    }

    private Properties loadProperties(String fileName) {
        Properties properties = new Properties();
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new FileNotFoundException("Nie znaleziono pliku konfiguracyjnego: " + fileName);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }

    private int getOrDefaultInt(Properties properties, String propertyName, int defaultValue) {
        return Integer.parseInt((String) properties.getOrDefault(propertyName, Integer.toString(defaultValue)));
    }

    /**
     * Zwraca czas przez jaki żeton potwierdzenia rejestracji jest ważny (w godzinach)
     */
    public int getRegistrationConfirmationTokenLifetime() {
        return getOrDefaultInt(propertiesToken, REGISTRATION_CONFIRMATION_TOKEN_LIFETIME, 24);
    }

    /**
     * Zwraca czas, przez jaki żeton zmiany adresu email jest ważny (w godzinach)
     */
    public int getEmailResetTokenLifetime() {
        return getOrDefaultInt(propertiesToken, EMAIL_RESET_TOKEN_LIFETIME, 24);
    }

    /**
     * Zwraca czas, przez jaki żeton resetu hasła jest ważny (w minutach)
     */
    public int getPasswordResetTokenLifetime() {
        return getOrDefaultInt(propertiesToken, PASSWORD_RESET_TOKEN_LIFETIME, 15);
    }

    /**
     * Zwraca czas, przez jaki żeton wymuszonego resetu hasła jest ważny (w godzinach)
     */
    public int getForcedPasswordResetTokenLifetime() {
        return getOrDefaultInt(propertiesToken, FORCED_PASSWORD_RESET_TOKEN_LIFETIME, 48);
    }

    /**
     * Zwraca czas, przez jaki żeton odblokowania własnego konta jest ważny (w godzinach)
     */
    public int getUnblockOwnAccountTokenLifetime() {
        return getOrDefaultInt(propertiesToken, UNBLOCK_OWN_ACCOUNT_TOKEN_LIFETIME, 168);
    }

    public int get2FaPeriod() {
        return Integer.parseInt(properties2Fa.getProperty(PERIOD_2FA));
    }

    public String getEmailApiKey() {
        return propertiesEmail.getProperty(EMAIL_API_KEY);
    }

    public String getEmailSenderName() {
        return propertiesEmail.getProperty(EMAIL_SENDER_NAME);
    }

    public String getEmailSenderAddress() {
        return propertiesEmail.getProperty(EMAIL_SENDER_ADDRESS);
    }

    public String getEmailAppName() {
        return propertiesEmail.getProperty(EMAIL_APP_NAME);
    }

    public String getEmailAppUrl() {
        return propertiesEmail.getProperty(EMAIL_APP_URL);
    }

    /**
     * Zwraca czas, co jaki następuje sprawdzenie, czy użytkownik w serwisie był nieaktywny przez dany czas (w godzinach)
     */
    public long getBlockCheckTimeout() {
        return Long.parseLong(propertiesTimeout.getProperty(BLOCK_CHECK_TIMEOUT));
    }

    /**
     * Zwraca czas, po jakim użytkownik będzie zablokowany z powodu braku aktywności (w dniach)
     */
    public long getBlockTimeout() {
        return Long.parseLong(propertiesTimeout.getProperty(BLOCK_TIMEOUT));
    }

    public int getTransactionRepetitionLimit() {
        return Integer.parseInt(propertiesTransaction.getProperty(TRANSACTION_REPETITION_LIMIT));
    }

    public String getRecaptchaApiKey() {
        return propertiesRecaptcha.getProperty(RECAPTCHA_API_KEY);
    }
}
