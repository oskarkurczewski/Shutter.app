package pl.lodz.p.it.ssbd2022.ssbd02.mok.service;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.Account;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.NoConfigFileFound;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.facade.AuthenticationFacade;
import pl.lodz.p.it.ssbd2022.ssbd02.util.ConfigLoader;
import pl.lodz.p.it.ssbd2022.ssbd02.util.EmailService;

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
    @Resource
    TimerService timerService;

    @Inject
    private AuthenticationFacade authenticationFacade;

    @Inject
    private VerificationTokenService verificationTokenService;

    @Inject
    private ConfigLoader configLoader;

    /**
     * Metoda inicjująca timer, aby działał co określoną w pliku
     * konfiguracyjnym liczbę milisekund, lub gdy nie będzie w stanie
     * odczytać pliku, co domyślną wartość 24 godzin wyrażoną w milisekundach
     */
    @PostConstruct
    public void init() {
        timerService.createTimer(0,
                configLoader.getBlockCheckTimeout() * 60 * 60 * 1000,
                "Co każdą ustaloną przez zmienną interval liczbę godzin");
    }

    /**
     * Metoda wywoływana co ustalony w pliku konfiguracyjnym czas
     * blokująca konta nieaktywne przez określony również w pliku czas
     * lub w przypadku braku możliwości odczytania wartości z pliku,
     * przez domyślną wartość 30dni
     */
    @Timeout
    public void blockUnusedAccounts() throws BaseApplicationException {
        LocalDateTime dateTime = LocalDateTime.now().minusDays(configLoader.getBlockTimeout());
        List<Account> accounts = authenticationFacade.getWithLastLoginBefore(dateTime);
        for (Account acc : accounts) {
            if (acc.getActive()) {
                acc.setActive(false);
                authenticationFacade.update(acc);
                verificationTokenService.sendUnblockOwnAccountToken(acc);
            }
        }
    }
}
