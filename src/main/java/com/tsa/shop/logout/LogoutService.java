package com.tsa.shop.logout;

import jakarta.servlet.http.Cookie;

public interface LogoutService {
    void logout(Cookie tokenCookie);
}
