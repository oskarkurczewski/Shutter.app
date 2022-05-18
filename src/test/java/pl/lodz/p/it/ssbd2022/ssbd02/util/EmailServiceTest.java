package pl.lodz.p.it.ssbd2022.ssbd02.util;

import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2022.ssbd02.exceptions.EmailException;

class EmailServiceTest {

    @Test
    void createEmail() throws EmailException {
        EmailService emailService = new EmailService();
        emailService.init();
        emailService.sendEmail("229983@edu.p.lodz.pl", "Example subject", "example body text");
    }
}