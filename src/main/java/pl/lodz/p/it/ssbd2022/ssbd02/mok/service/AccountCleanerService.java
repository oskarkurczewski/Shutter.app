package pl.lodz.p.it.ssbd2022.ssbd02.mok.service;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.TokenType;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.VerificationToken;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.facade.AuthenticationFacade;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.facade.TokenFacade;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;

@Startup
@Singleton
public class AccountCleanerService {

    @Inject
    private TokenFacade tokenFacade;

    @Inject
    private AuthenticationFacade authenticationFacade;

    @Resource
    TimerService timerService;

    @PostConstruct
    public void init() {
        long interval = 24 * 60 * 60 * 1000;
        timerService.createTimer(
                0,
                interval,
                "Co każdą ustaloną przez zmienną interval liczbę godzin"
        );
    }

    @Timeout
    public void cleanUnregisteredAccounts() throws BaseApplicationException {
        List<VerificationToken> expired = tokenFacade.findExpiredAfterOfType(
                TokenType.REGISTRATION_CONFIRMATION,
                LocalDateTime.now()
        );

        for (VerificationToken token : expired) {
            tokenFacade.remove(token);
            authenticationFacade.remove(token.getTargetUser());
        }
    }
}
