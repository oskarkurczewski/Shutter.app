package pl.lodz.p.it.ssbd2022.ssbd02.mok.service;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoConfigFileFound;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.facade.AuthenticationFacade;
import pl.lodz.p.it.ssbd2022.ssbd02.util.ConfigLoader;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.TimerService;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Klasa służąca do automatycznego blokowania kont, na które
 * nikt nie logował się przez określony czas
 */
@Startup
@Singleton
public class AccountBlockerService {
    private final ConfigLoader configLoader = new ConfigLoader();
    @Resource
    TimerService timerService;

    @Inject
    private AuthenticationFacade authenticationFacade;

    /**
     * Metoda inicjująca timer, aby działał co określoną w pliku
     * konfiguracyjnym liczbę milisekund, lub gdy nie będzie w stanie
     * odczytać pliku, co domyślną wartość 24 godzin wyrażoną w milisekundach
     */
    @PostConstruct
    public void init() {
        Long interval = null;
        try {
            interval = Long.parseLong(configLoader
                    .loadProperties("config.timeout.properties")
                    .getProperty("block.check-timeout"));
        } catch (NoConfigFileFound e) {
            interval = 86400000L;
        }
        timerService.createTimer(0,
                interval,
                "Co każdą ustaloną przez zmienną interval liczbę milisekund");
    }

    /**
     * Metoda wywoływana co ustalony w pliku konfiguracyjnym czas
     * blokująca konta nieaktywne przez określony również w pliku czas
     * lub w przypadku braku możliwości odczytania wartości z pliku,
     * przez domyślną wartość 30dni
     */
    @Timeout
    public void blockUnusedAccounts() throws BaseApplicationException {
        Long days = null;
        try {
            days = Long.parseLong(configLoader
                    .loadProperties("config.timeout.properties")
                    .getProperty("block.timeout"));
        } catch (NoConfigFileFound e) {
            days = 30L;
        }
        LocalDateTime dateTime = LocalDateTime.now().minusDays(days);
        List<Account> accounts = authenticationFacade.getWithLastLoginBefore(dateTime);
        for (Account acc : accounts) {
            acc.setActive(false);
            authenticationFacade.update(acc);
        }
    }
}
