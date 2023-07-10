package com.tsa.shop.logout;

import com.tsa.shop.login.repo.TokenRepository;
import jakarta.servlet.http.Cookie;

public class DefaultLogoutService implements LogoutService {

    private final TokenRepository tokenRepository;

    public DefaultLogoutService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void logout(Cookie tokenCookie) {
        tokenRepository.deleteToken(tokenCookie.getValue());
    }
}
