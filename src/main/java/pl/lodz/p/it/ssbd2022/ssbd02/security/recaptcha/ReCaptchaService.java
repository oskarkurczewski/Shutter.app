package pl.lodz.p.it.ssbd2022.ssbd02.security.recaptcha;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.ExceptionFactory;
import pl.lodz.p.it.ssbd2022.ssbd02.util.ConfigLoader;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.util.regex.Pattern;

@Stateless
public class ReCaptchaService {

    @Inject
    private ConfigLoader configLoader;

    @Context
    private HttpServletRequest httpServletRequest;

    public static String RECAPTCHA_API_URL = "https://www.google.com/recaptcha/api/siteverify";

    private static final Pattern RESPONSE_REGEX = Pattern.compile("[A-Za-z0-9_-]+");

    @PermitAll
    public void verify(String res) throws BaseApplicationException {
        if (res.length() == 0 || !RESPONSE_REGEX.matcher(res).matches()) {
            throw ExceptionFactory.invalidRecaptchaException();
        }

        try {
            String response = Unirest.post(RECAPTCHA_API_URL)
                    .field("secret", configLoader.getRecaptchaApiKey())
                    .field("response", res)
                    .asString().getBody();

            ObjectMapper mapper = new ObjectMapper();
            GoogleResponse googleResponse = mapper.readValue(response, GoogleResponse.class);

            if (!googleResponse.isSuccess()) {
                throw ExceptionFactory.invalidRecaptchaException();
            }
        } catch (UnirestException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
