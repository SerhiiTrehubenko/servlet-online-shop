package com.tsa.shop.login.impl;

import com.tsa.shop.login.interfaces.Responder;
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
