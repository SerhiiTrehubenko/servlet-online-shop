package com.tsa.shop.login.interfaces;

import jakarta.servlet.http.Cookie;

public interface CookieFactory {
    Cookie makeUserTokenCookie(String token);
}
