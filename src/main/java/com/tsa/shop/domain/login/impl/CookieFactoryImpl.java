package com.tsa.shop.domain.login.impl;

import com.tsa.shop.domain.login.interfaces.CookieFactory;
import jakarta.servlet.http.Cookie;

public class CookieFactoryImpl implements CookieFactory {

    private final static String TOKEN_PARAMETER = "user-token";
    @Override
    public Cookie makeUserTokenCookie(String token) {
        return new Cookie(TOKEN_PARAMETER, token);
    }
}
