package com.tsa.shop.login.impl;

import com.tsa.shop.transaction.Command;
import com.tsa.shop.login.interfaces.*;
import com.tsa.shop.servlets.enums.HttpStatus;
import com.tsa.shop.exceptions.WebServerException;
import jakarta.servlet.http.Cookie;

import java.util.Objects;
import java.util.UUID;

public class LogInTransaction implements Command {
    private final IncomeDataProvider incomeDataProvider;
    private final UserRepository userRepository;
    private final PasswordHashGenerator passwordHashGenerator;
    private final TokenGenerator tokenGenerator;
    private final TokenRepository tokenRepository;
    private final Responder responder;
    private final CookieFactory cookieFactory;

    private String name = "";
    private String password = "";
    private User user;
    private UUID token;

    private boolean authorized = false;

    public LogInTransaction(IncomeDataProvider incomeDataProvider,
                            UserRepository userRepository,
                            PasswordHashGenerator passwordHashGenerator,
                            TokenGenerator tokenGenerator,
                            TokenRepository tokenRepository,
                            Responder responder,
                            CookieFactory cookieFactory) {
        this.incomeDataProvider = incomeDataProvider;
        this.userRepository = userRepository;
        this.passwordHashGenerator = passwordHashGenerator;
        this.tokenGenerator = tokenGenerator;
        this.tokenRepository = tokenRepository;
        this.responder = responder;
        this.cookieFactory = cookieFactory;
    }

    @Override
    public void execute() {

        setProperties();
        requiredNotEmptyProperties();

        getUserFromRepository();
        isUserPresent();

        validatePassword();
        isAuthorized();

        makeToken();
        addTokenToRepository();

        setUserTokenCookie();
    }

    private void setUserTokenCookie() {
        Cookie cookie = cookieFactory.makeUserTokenCookie(token.toString());
        responder.set(cookie);
    }

    private void addTokenToRepository() {
        tokenRepository.add(token);
    }

    private void makeToken() {
        token = tokenGenerator.generate();
    }

    private void isAuthorized() {
        if (!authorized) {
            throw badNameOrPasswordException();
        }
    }

    WebServerException badNameOrPasswordException() {
        return new WebServerException("You have provided wrong Name or Password", HttpStatus.BAD_REQUEST, this);
    }

    private void validatePassword() {
        String sole = user.getSole();
        String hashMd5FromUser = user.getPasswordMD5();
        String hashMd5BasedOnIncomePassword = passwordHashGenerator.generateMD5(password, sole);
        if (Objects.equals(hashMd5FromUser, hashMd5BasedOnIncomePassword)) {
            authorized = true;
        }
    }

    private void isUserPresent() {
        if (Objects.isNull(user)) {
            throw badNameOrPasswordException();
        }
    }


    private void requiredNotEmptyProperties() {
        if (name.isEmpty() || password.isEmpty()) {
            throw new WebServerException("Fields of Name or Password cannot be empty", HttpStatus.BAD_REQUEST, this);
        }
    }

    private void getUserFromRepository() {
        user = userRepository.getUser(name);
    }

    private void setProperties() {
        name = incomeDataProvider.getName();
        password = incomeDataProvider.getPassword();
    }
}
