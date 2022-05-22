package pl.lodz.p.it.ssbd2022.ssbd02.util;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.VerificationToken;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.EmailException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.ExceptionFactory;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoConfigFileFound;
import sendinblue.ApiClient;
import sendinblue.ApiException;
import sendinblue.Configuration;
import sendinblue.auth.ApiKeyAuth;
import sibApi.TransactionalEmailsApi;
import sibModel.CreateSmtpEmail;
import sibModel.SendSmtpEmail;
import sibModel.SendSmtpEmailSender;
import sibModel.SendSmtpEmailTo;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.util.Collections;
import java.util.Properties;

/**
 * Klasa służąca do wysyłania maili
 */
@Stateless
@Interceptors(LoggingInterceptor.class)
public class EmailService {

    private static final String CONFIG_FILE_NAME = "config.email.properties";
    private static final String BASE_URL = "http://studapp.it.p.lodz.pl:8002";

    private TransactionalEmailsApi api;
    private SendSmtpEmailSender sender;
    @Inject
    private ConfigLoader configLoader;
    private Properties properties;


    @PostConstruct
    public void init() {
        ApiClient defaultClient = Configuration.getDefaultApiClient();

        try {
            configLoader = new ConfigLoader();
            properties = configLoader.loadProperties(CONFIG_FILE_NAME);
        } catch (NoConfigFileFound e) {
            throw new RuntimeException(e);
        }

        // Configure API key authorization: api-key
        ApiKeyAuth apiKey = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
        apiKey.setApiKey(properties.getProperty("api.key"));

        api = new TransactionalEmailsApi();
        sender = new SendSmtpEmailSender();
        sender.setEmail(properties.getProperty("email.sender.email"));
        sender.setName(properties.getProperty("email.sender.name"));
    }

    /**
     * Przykładowa funkcja korzystająca z funkcji sendMail
     *
     * @param to    adresat wiadomości email
     * @param token Obiekt przedstawiający żeton weryfikacyjny użyty do potwierdzenia rejestracji
     */
    public void sendRegistrationEmail(String to, VerificationToken token) {
        String subject = "Weryfikacja konta Shutter.app";
        String body = "Kliknij w link aby potwierdzić rejestrację swojego konta: " + String.format(
                "%s/confirm/%s",
                BASE_URL,
                token.getToken()
        );
        try {
            sendEmail(to, subject, body);
        } catch (EmailException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Funkcja wysyłająca do wskazanego użytkownika maila z przypomnieniem o konieczności potwierdzenia rejestracji
     *
     * @param to    adresat wiadomości email
     * @param token Obiekt przedstawiający żeton weryfikacyjny użyty do potwierdzenia rejestracji
     */
    public void sendRegistrationConfirmationReminder(String to, VerificationToken token) {
        String subject = "PRZYPOMNIENIE: Weryfikacja konta Shutter.app";
        String body = "Przypominamy o konieczności potwierdzenia konta w Shutter.app. " +
                "Kliknij w link aby potwierdzić rejestrację swojego konta: " + String.format(
                "%s/confirm/%s",
                BASE_URL,
                token.getToken()
        );
        try {
            sendEmail(to, subject, body);
        } catch (EmailException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Wysyła na adres email podany jako parametr żeton weryfikacyjny resetowania hasła
     *
     * @param to    Adres e-mail, na który wysłany ma zostać wiadomość zawierająca żeton
     * @param login Login użytkownika, którego hasło ma zostać zresetowane
     * @param token Żeton, który ma zostać wysłany
     */
    public void sendPasswordResetEmail(String to, String login, VerificationToken token) {
        String subject = "Resetowanie hasła Shutter.app";
        String body = "Kliknij w link aby dokonać resetu hasła: " + String.format(
                "%s/%s/password-reset/%s",
                BASE_URL,
                login,
                token.getToken()
        );
        try {
            sendEmail(to, subject, body);
        } catch (EmailException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Wysyła na adres email podany jako parametr żeton weryfikacyjny resetowania hasła w przypadku kiedy
     * zostanie ono zmienione przez administratora systemu
     *
     * @param to    Adres e-mail, na który wysłany ma zostać wiadomość zawierająca żeton
     * @param login Login użytkownika, którego hasło ma zostać zresetowane
     * @param token Żeton, który ma zostać wysłany
     */
    public void sendForcedPasswordResetEmail(String to, String login, VerificationToken token) {
        String subject = "WAŻNE: Konieczność zmiany hasła - Shutter.app";
        String body = "Twoje hasło zostało zmienione przez administratora. Aby ustawić nowe hasło dla " +
                "twojego konta kliknij w podany link: " + String.format(
                "%s/%s/password-reset/%s",
                BASE_URL,
                login,
                token.getToken()
        );
        try {
            sendEmail(to, subject, body);
        } catch (EmailException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Wysyła email powiadamiający użytkownika o zablokowaniu jego konta z powodu zbyt wielu nieudanych
     * prób logowania
     *
     * @param to Adres e-mail, na który należy wysłać wiadomość
     */
    public void sendAccountBlockedDueToToManyLogInAttemptsEmail(String to) {
        String subject = "Zbyt wiele nieudanych logowań - Shutter.app";
        String body = "Użytkowniku, twoje konto zostało automatycznie zablokowane z powodu zbyt wielu " +
                "nieudanych prób logowania. Aby je odblokować skontaktuj się z administratorem.";
        try {
            sendEmail(to, subject, body);
        } catch (EmailException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Wysyła na podany adres email ostrzeżenie o logowaniu na konto administratora
     *
     * @param to        email, na który zostać ma wysłane powiadomienie. Powinien być to email administratora systemu
     * @param login     login użytkownika, na którego email ma zostać przesłane powiadomienie
     * @param ipAddress adres IP, z którego dokonano logowania na konto administratora
     */
    public void sendAdminAuthenticationWaringEmail(String to, String login, String ipAddress) {
        String subject = "WAŻNE: Logowanie na konto - Shutter.app";
        String body = String.format(
                "Administratorze %s, doszło do uwierzytelnienia na twoje konto z adresu IP: %s.",
                login,
                ipAddress
        );
        try {
            sendEmail(to, subject, body);
        } catch (EmailException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Wysyła na adres email podany jako parametr żeton weryfikacyjny do aktualizacji adresu email
     *
     * @param to    Adres e-mail, na który wysłany ma zostać wiadomość zawierająca żeton
     * @param token Żeton, który ma zostać wysłany
     */
    public void sendEmailUpdateEmail(String to, String login, VerificationToken token) {
        String subject = "Zmiana adresu e-mail Shutter.app";
        String body = "Kliknij w link aby dokonać aktualizacji adresu email: " + String.format(
                "%s/%s/email-update/%s",
                BASE_URL,
                login,
                token.getToken()
        );
        try {
            sendEmail(to, subject, body);
        } catch (EmailException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Funkcja wysyłająca na podany adres email informację o tym, że konto użytkownika z nim powiązane zostało
     * odblokowane w systemie.
     *
     * @param to adresat wiadomości email
     */
    public void sendAccountUnblockedEmail(String to) {
        String subject = "Konto odblokowane - Shutter.app";
        String body = "Twoje konto w aplikacji Shutter.app zostało odblokowane. Życzymy miłego dalszego " +
                "korzystania z usługi.";
        try {
            sendEmail(to, subject, body);
        } catch (EmailException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Funkcja wysyłająca na podany adres email informację o tym, że konto użytkownika z nim powiązane zostało
     * zablokowane w systemie.
     *
     * @param to adresat wiadomości email
     */
    public void sendAccountBlocked(String to) {
        String subject = "Konto zablokowane - Shutter.app";
        String body = "Twoje konto w aplikacji Shutter.app zostało zablokowane. Aby je " +
                "odblokować skontaktuj się z administratorem systemu.";
        try {
            sendEmail(to, subject, body);
        } catch (EmailException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Funkcja wysyłająca na podany adres email informację o tym, że danemu użytkownikowi o tym adresie
     * email został przypisany nowy poziom dostępu.
     *
     * @param to              adresat wiadomości email
     * @param accessLevelName nazwa poziomu dostępu, który został przypisany
     */
    public void sendAccessLevelGrantedEmail(String to, String accessLevelName) {
        String subject = "Przypisanie poziomu dostepu - Shutter.app";
        String body = "Do twojego konta został przypisany poziom dostępu: " + accessLevelName + ".";
        try {
            sendEmail(to, subject, body);
        } catch (EmailException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Funkcja wysyłająca na podany adres email informację o tym, że danemu użytkownikowi o tym adresie
     * email został odebrany poziom dostępu
     *
     * @param to              adresat wiadomości email
     * @param accessLevelName nazwa poziomu dostępu, który został odebrany
     */
    public void sendAccessLevelRevokedEmail(String to, String accessLevelName) {
        String subject = "Odebranie poziomu dostepu - Shutter.app";
        String body = "Dla twojego konta został odebrany poziom dostępu: " + accessLevelName + ".";
        try {
            sendEmail(to, subject, body);
        } catch (EmailException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Funkcja wysyłająca na podany adres email informację o tym, że konto użytkownika z nim powiązane zostało
     * aktywowane w systemie.
     *
     * @param to adresat wiadomości email
     */
    public void sendAccountActivated(String to) {
        String subject = "Konto aktywowane - Shutter.app";
        String body = "Twoje konto w aplikacji Shutter.app zostało pomyślnie aktywowane. Życzymy miłego dalszego " +
                "korzystania z usługi.";
        try {
            sendEmail(to, subject, body);
        } catch (EmailException e) {
            throw new RuntimeException(e);
        }
    }




    /**
     * Funkcja służąca do wysyłania emaili
     *
     * @param toEmail  adres email odbiorcy
     * @param subject  tytuł emaila
     * @param bodyText treść emaila w html
     * @see "https://developers.sendinblue.com/reference/sendtransacemail/"
     */
    public void sendEmail(String toEmail,
                          String subject,
                          String bodyText) throws EmailException {

        SendSmtpEmailTo to = new SendSmtpEmailTo();
        to.setEmail(toEmail);

        Properties params = new Properties();
        params.setProperty("parameter", bodyText);
        params.setProperty("subject", subject);


        SendSmtpEmail sendSmtpEmail = new SendSmtpEmail();
        sendSmtpEmail.setParams(params);
        sendSmtpEmail.setSender(sender);
        sendSmtpEmail.setTo(Collections.singletonList(to));
        sendSmtpEmail.setHtmlContent("<html><body>{{params.parameter}}</body></html>");
        sendSmtpEmail.setSubject("{{params.subject}}");

        try {
            CreateSmtpEmail response = api.sendTransacEmail(sendSmtpEmail);
        } catch (ApiException e) {
            throw ExceptionFactory.emailException(e.getMessage());
        }
    }

}