package com.tsa.shop.login.interfaces;

import jakarta.servlet.http.Cookie;

public interface Responder {
    void set(Cookie cookie);

    Cookie getCookie();
}
