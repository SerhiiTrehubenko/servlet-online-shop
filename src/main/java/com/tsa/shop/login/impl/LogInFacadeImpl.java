package com.tsa.shop.login.impl;

import com.tsa.shop.login.repo.TokenRepository;
import com.tsa.shop.login.repo.UserRepository;
import com.tsa.shop.transaction.Command;
import com.tsa.shop.login.interfaces.*;
import javax.servlet.http.Cookie;

import java.util.Map;

public class LogInFacadeImpl implements LogInFacade {
    private final LogInFactory logInFactory;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordHashGenerator passwordValidator;
    private final TokenGenerator tokenGenerator;
    private final CookieFactory cookieFactory;

    public LogInFacadeImpl(LogInFactory logInFactory,
                           UserRepository userRepository,
                           TokenRepository tokenRepository) {
        this.logInFactory = logInFactory;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        passwordValidator = logInFactory.makePasswordHashGenerator();
        tokenGenerator = logInFactory.makeTokenGenerator();
        cookieFactory = logInFactory.makeCookieGenerator();
    }

    @Override
    public Cookie process(Map<String, String[]> parameters) {
        IncomeDataProvider incomeDataProvider = logInFactory.makeDataProvider(parameters);
        Responder responder = logInFactory.makeResponder();
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
