package com.tsa.shop.domain.login.impl;

import com.tsa.shop.domain.login.interfaces.LogInFacade;
import com.tsa.shop.domain.login.interfaces.LogInFactory;
import com.tsa.shop.domain.login.interfaces.TokenRepository;
import com.tsa.shop.domain.login.interfaces.UserRepository;
import jakarta.servlet.http.Cookie;
import org.eclipse.jetty.util.security.Credential;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class LogInFacadeITest {
    private final String email = "tsa@gmaol.com";
    private final String password = "password";
    private final UserRepository userRepository = new UserRepositoryImpl();
    private final TokenRepository tokenRepository = new DefaultTokenRepository();

    @BeforeEach
    void setUp() {
        UUID sole = UUID.randomUUID();
        String passwordPlusSoleHash = Credential.MD5.getCredential(password + sole).toString();
        User user = new User(email, passwordPlusSoleHash, sole.toString());
        userRepository.addUser(user);
    }

    @Test
    void shouldReturnCookieAfterProcessOfAuthentication() {

        UUID expectedToken = UUID.randomUUID();
        tokenRepository.add(expectedToken);

        LogInFactory logInFactory = new LogInFactoryImpl();
        LogInFacade logInFacade = new LogInFacadeImpl(logInFactory, userRepository, tokenRepository);

        Cookie responder = logInFacade.process(Map.of("email", new String[]{email}, "password", new String[]{password}));

        assertNotNull(responder);
        assertEquals("user-token", responder.getName());
        assertNotNull(responder.getValue());
        assertFalse(responder.getValue().isEmpty());
        assertEquals(36, responder.getValue().length());
    }
}
