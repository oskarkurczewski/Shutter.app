package pl.lodz.p.it.ssbd2022.ssbd02.util;

import at.favre.lib.crypto.bcrypt.BCrypt;

import javax.security.enterprise.identitystore.PasswordHash;
import java.nio.charset.StandardCharsets;

public class PasswordHashImpl implements PasswordHash {
    @Override
    public String generate(char[] chars) {
        return BCrypt.withDefaults().hashToString(6, chars);
    }

    @Override
    public boolean verify(char[] chars, String s) {
        return BCrypt.verifyer().verify(chars, s.getBytes(StandardCharsets.UTF_8)).verified;
    }
}
