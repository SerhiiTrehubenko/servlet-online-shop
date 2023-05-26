package com.tsa.shop.domain.login.impl;

import com.tsa.shop.domain.interfaces.Command;
import com.tsa.shop.domain.login.interfaces.*;


import java.util.Map;

public class LogInFactoryImpl implements LogInFactory {
    @Override
    public IncomeDataProvider makeDataProvider(Map<String, String[]> parameters) {
        return new IncomeDataProviderImpl(parameters);
    }

    @Override
    public PasswordHashGenerator makePasswordHashGenerator() {
        return new PasswordHashGeneratorImpl();
    }

    @Override
    public TokenGenerator makeTokenGenerator() {
        return new DefaultTokenGenerator();
    }

    @Override
    public Responder makeResponder() {
        return new ResponderImpl();
    }

    @Override
    public CookieFactory makeCookieGenerator() {
        return new CookieFactoryImpl();
    }

    @Override
    public Command makeLogInTransaction(IncomeDataProvider incomeDataProvider, UserRepository userRepository, PasswordHashGenerator passwordHashGenerator, TokenGenerator tokenGenerator, TokenRepository tokenRepository, Responder responder, CookieFactory cookieFactory) {
        return new LogInTransaction(incomeDataProvider, userRepository, passwordHashGenerator, tokenGenerator, tokenRepository, responder, cookieFactory);
    }
}
