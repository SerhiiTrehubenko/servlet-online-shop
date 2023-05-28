package com.tsa.shop.login.interfaces;

import com.tsa.shop.login.repo.TokenRepository;
import com.tsa.shop.login.repo.UserRepository;
import com.tsa.shop.transaction.Command;

import java.util.Map;

public interface LogInFactory {
    IncomeDataProvider makeDataProvider(Map<String, String[]> parameters);

    PasswordHashGenerator makePasswordHashGenerator();

    TokenGenerator makeTokenGenerator();

    Responder makeResponder();

    CookieFactory makeCookieGenerator();

    Command makeLogInTransaction(IncomeDataProvider incomeDataProvider,
                                 UserRepository userRepository,
                                 PasswordHashGenerator passwordHashGenerator,
                                 TokenGenerator tokenGenerator,
                                 TokenRepository tokenRepository,
                                 Responder responder,
                                 CookieFactory cookieFactory);
}
