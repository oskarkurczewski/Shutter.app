package pl.lodz.p.it.ssbd2022.ssbd02.util;

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
     * @param to adresat wiadomości email
     */
    public void sendRegistrationEmail(String to) {
        // logic

        // String body = properties.getProperty("email.registrationemail.body")
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
        sendSmtpEmail.setHtmlContent("<html><body><h1>This is my first transactional email {{params.parameter}}</h1></body></html>");
        sendSmtpEmail.setSubject("My {{params.subject}}");

        try {
            CreateSmtpEmail response = api.sendTransacEmail(sendSmtpEmail);
        } catch (ApiException e) {
            throw ExceptionFactory.emailException(e.getMessage());
        }
    }
}