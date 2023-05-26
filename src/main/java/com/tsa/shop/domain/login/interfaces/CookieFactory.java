package com.tsa.shop.domain.login.interfaces;

import jakarta.servlet.http.Cookie;

public interface CookieFactory {
    Cookie makeUserTokenCookie(String token);
}
