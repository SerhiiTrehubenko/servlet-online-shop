package com.tsa.shop.login.impl;

import com.tsa.shop.login.interfaces.CookieFactory;
import javax.servlet.http.Cookie;

public class CookieFactoryImpl implements CookieFactory {

    private final static String TOKEN_PARAMETER = "user-token";
    @Override
    public Cookie makeUserTokenCookie(String token) {
        return new Cookie(TOKEN_PARAMETER, token);
    }
}
