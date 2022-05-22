package pl.lodz.p.it.ssbd2022.ssbd02.util;

import org.junit.jupiter.api.Test;
import pl.lodz.p.it.ssbd2022.ssbd02.security.OneTimeCodeUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class OneTimePasswordTest {

    public static String testSecret = "VV3KOX7UQJ4KYAKOHMZPPH3US4CJIMH6F3ZKNB5C2OOBQ6V2KIYHM27Q";
    @Test
    public void createAndValidateToken() {
        OneTimeCodeUtils otp = new OneTimeCodeUtils();
        String code = otp.generateCode(testSecret);
        assertNotNull(code);
        assertEquals(6, code.length());
        Boolean valid = otp.verifyCode(testSecret, code);
        assertEquals(valid, Boolean.TRUE);
//         Change period in properties to 1 second before running it
//        Thread.sleep(1500);
//        Boolean validOther = otp.verifyCode(testSecret, code);
//        assertEquals(validOther, Boolean.FALSE);
    }
}
