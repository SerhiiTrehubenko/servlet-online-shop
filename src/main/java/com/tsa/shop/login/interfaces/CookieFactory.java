package com.tsa.shop.login.interfaces;

import javax.servlet.http.Cookie;

public interface CookieFactory {
    Cookie makeUserTokenCookie(String token);
}
