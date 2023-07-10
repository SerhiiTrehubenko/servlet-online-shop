package com.tsa.shop.login.impl;

import com.tsa.shop.domain.Session;
import com.tsa.shop.login.repo.TokenRepository;
import com.tsa.shop.login.repo.UserRepository;
import com.tsa.shop.transaction.Command;
import com.tsa.shop.domain.User;
import com.tsa.shop.login.interfaces.*;
import com.tsa.shop.domain.WebServerException;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class LogInTransactionTest {

    private final String name = "tsa@gmail.com";
    private final String password = "1234";
    private final User user = mock(User.class);
    private final String sole = "sole";
    private IncomeDataProvider incomeDataProvider;
    private UserRepository userRepository;
    private PasswordHashGenerator passwordHashGenerator;
    private TokenGenerator tokenGenerator;
    private TokenRepository tokenRepository;
    private Responder responder;
    private CookieFactory cookieFactory;
    private Command logInTransactionSut;

    @BeforeEach
    void setUp() {

        incomeDataProvider = mock(IncomeDataProvider.class);
        userRepository = mock(UserRepository.class);
        passwordHashGenerator = mock(PasswordHashGenerator.class);
        tokenGenerator = mock(TokenGenerator.class);
        tokenRepository = mock(TokenRepository.class);
        responder = mock(Responder.class);
        cookieFactory = mock(CookieFactory.class);

        logInTransactionSut = new LogInTransaction(incomeDataProvider,
                userRepository,
                passwordHashGenerator,
                tokenGenerator,
                tokenRepository,
                responder,
                cookieFactory);

        when(tokenGenerator.generate()).thenReturn(UUID.randomUUID());
        when(cookieFactory.makeUserTokenCookie(anyString())).thenReturn(new Cookie("user-token", "generated-uuid-token"));
        when(user.getSole()).thenReturn(sole);
        when(user.getPasswordMD5()).thenReturn("MD5Hash");
        when(incomeDataProvider.getName()).thenReturn(name);
        when(incomeDataProvider.getPassword()).thenReturn(password);
        when(userRepository.getUser(name)).thenReturn(user);
        when(passwordHashGenerator.generateMD5(password, sole)).thenReturn("MD5Hash");
    }

    @Test
    void shouldCallGetNameAndGetPasswordMethodsOnIncomeDataProvider() {

        logInTransactionSut.execute();

        verify(incomeDataProvider).getName();
        verify(incomeDataProvider).getPassword();
    }

    @Test
    void shouldThrowExceptionWhenNameIsEmpty() {
        when(incomeDataProvider.getName()).thenReturn("");
        when(incomeDataProvider.getPassword()).thenReturn("password");

        assertThrows(WebServerException.class, logInTransactionSut::execute);
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsEmpty() {
        when(incomeDataProvider.getName()).thenReturn("name");
        when(incomeDataProvider.getPassword()).thenReturn("");

        assertThrows(WebServerException.class, logInTransactionSut::execute);
    }

    @Test
    void shouldThrowExceptionWhenUserWasNotFound() {
        when(userRepository.getUser(name)).thenReturn(null);

        assertThrows(WebServerException.class, logInTransactionSut::execute);
    }

    @Test
    void shouldCallGetUserOnUserRepository() {

        logInTransactionSut.execute();

        verify(userRepository).getUser(name);
    }

    @Test
    void shouldCallGenerateMethodOnTokenGenerator() {
        logInTransactionSut.execute();

        verify(tokenGenerator).generate();
    }

    @Test
    void shouldCallAddMethodOnTokenRepository() {
        UUID expected = UUID.randomUUID();

        when(tokenGenerator.generate()).thenReturn(expected);

        logInTransactionSut.execute();

        verify(tokenRepository).add(any(Session.class));
    }

    @Test
    void shouldCallMakeUserTokenCookieOnCookieFactory() {
        UUID generatedUuid = UUID.randomUUID();
        Cookie generatedCookie = new Cookie("user-token", generatedUuid.toString());
        when(tokenGenerator.generate()).thenReturn(generatedUuid);
        when(cookieFactory.makeUserTokenCookie(generatedUuid.toString())).thenReturn(generatedCookie);

        logInTransactionSut.execute();

        verify(cookieFactory).makeUserTokenCookie(generatedUuid.toString());
    }

    @Test
    void shouldCallSetMethodOnResponder() {
        UUID generatedUuid = UUID.randomUUID();
        Cookie generatedCookie = new Cookie("user-token", generatedUuid.toString());
        when(tokenGenerator.generate()).thenReturn(generatedUuid);
        when(cookieFactory.makeUserTokenCookie(generatedUuid.toString())).thenReturn(generatedCookie);

        logInTransactionSut.execute();

        verify(responder).set(generatedCookie);
    }
}
