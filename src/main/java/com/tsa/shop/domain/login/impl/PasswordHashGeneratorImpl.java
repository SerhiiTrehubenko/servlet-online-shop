package com.tsa.shop.domain.login.impl;

import com.tsa.shop.domain.login.interfaces.PasswordHashGenerator;
import org.eclipse.jetty.util.security.Credential;

public class PasswordHashGeneratorImpl implements PasswordHashGenerator {
    @Override
    public String generateMD5(String password, String sole) {
        return Credential.MD5.getCredential(password + sole).toString();
    }
}
