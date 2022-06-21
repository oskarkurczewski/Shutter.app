package pl.lodz.p.it.ssbd2022.ssbd02.security;

import javax.security.enterprise.identitystore.PasswordHash;

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
