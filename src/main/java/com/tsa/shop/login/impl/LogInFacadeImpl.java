package com.tsa.shop.login.impl;

import com.tsa.shop.login.repo.TokenRepository;
import com.tsa.shop.login.repo.UserRepository;
import com.tsa.shop.transaction.Command;
import com.tsa.shop.login.interfaces.*;
import jakarta.servlet.http.Cookie;

import java.util.Map;

public class LogInFacadeImpl implements LogInFacade {

    private final LogInFactory logInFactory;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    public LogInFacadeImpl(LogInFactory logInFactory,
                           UserRepository userRepository,
                           TokenRepository tokenRepository) {
        this.logInFactory = logInFactory;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
    }

    @Override
    public Cookie process(Map<String, String[]> parameters) {
        IncomeDataProvider incomeDataProvider = logInFactory.makeDataProvider(parameters);
        PasswordHashGenerator passwordValidator = logInFactory.makePasswordHashGenerator();
        TokenGenerator tokenGenerator = logInFactory.makeTokenGenerator();
        Responder responder = logInFactory.makeResponder();
        CookieFactory cookieFactory = logInFactory.makeCookieGenerator();

        Command loginTransaction =
                logInFactory.makeLogInTransaction(incomeDataProvider,
                        userRepository,
                        passwordValidator,
                        tokenGenerator,
                        tokenRepository,
                        responder,
                        cookieFactory);

        loginTransaction.execute();

        return responder.getCookie();
    }
}
