package pl.lodz.p.it.ssbd2022.ssbd02.util;

import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2022.ssbd02.security.OneTimeCodeUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OneTimePasswordTest {
    @Test
    public void createAndValidateToken() throws InterruptedException {
        OneTimeCodeUtils otp = new OneTimeCodeUtils();
        otp.setConfigLoader(new ConfigLoader());
        otp.init();
        String code = otp.generateCode();
        assertNotNull(code);
        assertEquals(6, code.length());
        Boolean valid = otp.verifyCode(code);
        assertEquals(valid, Boolean.TRUE);
//         Change period in properties to 1 second before running it
//        Thread.sleep(1500);
//        Boolean validOther = otp.verifyCode(code);
//        assertEquals(validOther, Boolean.FALSE);
    }
}
