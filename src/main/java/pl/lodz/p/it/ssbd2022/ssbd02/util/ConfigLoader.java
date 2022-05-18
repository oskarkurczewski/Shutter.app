package pl.lodz.p.it.ssbd2022.ssbd02.util;

import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.ExceptionFactory;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoConfigFileFound;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Klasa służąca do wczytywania plików konfiguracyjnych
 */
@Stateless
@Interceptors(LoggingInterceptor.class)
public class ConfigLoader {

    public ConfigLoader() {
    }

    public Properties loadProperties(String fileName) throws NoConfigFileFound {
        Properties properties = new Properties();
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw ExceptionFactory.noConfigFileFound();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }

}
