package pl.lodz.p.it.ssbd2022.ssbd02.security;

import at.favre.lib.crypto.bcrypt.BCrypt;

import javax.security.enterprise.identitystore.PasswordHash;
import java.nio.charset.StandardCharsets;

public class PasswordHashImpl implements PasswordHash {
    @Override
    public String generate(char[] chars) {
        return BCryptUtils.generate(chars);
    }

    @Override
    public boolean verify(char[] chars, String s) {
        return BCryptUtils.verify(chars, s);
    }
}
