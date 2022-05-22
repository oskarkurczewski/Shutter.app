package pl.lodz.p.it.ssbd2022.ssbd02.mok.service;

import pl.lodz.p.it.ssbd2022.ssbd02.entity.TokenType;
import pl.lodz.p.it.ssbd2022.ssbd02.entity.VerificationToken;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.BaseApplicationException;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.facade.AuthenticationFacade;
import pl.lodz.p.it.ssbd2022.ssbd02.mok.facade.TokenFacade;
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

@Startup
@Singleton
public class AutoReminderService {

    @Inject
    private EmailService emailService;

    @Inject
    private TokenFacade tokenFacade;

    @Resource
    TimerService timerService;

    @PostConstruct
    public void init() {
        long interval = 12 * 60 * 60 * 1000;
        timerService.createTimer(
                0,
                interval,
                "Co każdą ustaloną przez zmienną interval liczbę godzin"
        );
    }

    @Timeout
    public void remindRegistrationConfirmation() {
        List<VerificationToken> toRemindTokens = null;
        try {
            toRemindTokens = tokenFacade.findExpiredAfterOfType(
                    TokenType.REGISTRATION_CONFIRMATION,
                    LocalDateTime.now().plusHours(12)
            );
        } catch (BaseApplicationException ex) {
            return;
        }

        for (VerificationToken token : toRemindTokens) {
            emailService.sendRegistrationConfirmationReminder(
                    token.getTargetUser().getEmail(),
                    token
            );
        }
    }
}
