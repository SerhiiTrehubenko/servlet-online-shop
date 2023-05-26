package com.tsa.shop.domain.login.impl;

import com.tsa.shop.domain.login.interfaces.Responder;
import jakarta.servlet.http.Cookie;

public class ResponderImpl implements Responder {

    private Cookie cookie;

    @Override
    public void set(Cookie cookie) {
        this.cookie = cookie;
    }

    @Override
    public Cookie getCookie() {
        return cookie;
    }
}
