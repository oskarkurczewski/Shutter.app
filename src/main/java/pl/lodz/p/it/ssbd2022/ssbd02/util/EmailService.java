package pl.lodz.p.it.ssbd2022.ssbd02.util;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.VerificationToken;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.EmailException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.ExceptionFactory;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoConfigFileFound;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.facade.TokenFacade;
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
    @Inject TokenFacade tokenFacade;


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
     * @param to adresat wiadomości email
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
     * Wysyła na adres email podany jako parametr żeton weryfikacyjny resetowania hasła
     *
     * @param to    Adres e-mail, na który wysłany ma zostać wiadomość zawierająca żeton
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