package pl.lodz.p.it.ssbd2022.ssbd02.security;

import at.favre.lib.crypto.bcrypt.BCrypt;

import java.nio.charset.StandardCharsets;

public class BCryptUtils {

    public static String generate(char[] chars) {
        return BCrypt.withDefaults().hashToString(6, chars);
    }

    public static boolean verify(char[] chars, String s) {
        return BCrypt.verifyer().verify(chars, s.getBytes(StandardCharsets.UTF_8)).verified;
    }
}
