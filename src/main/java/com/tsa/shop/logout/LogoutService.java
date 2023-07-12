package com.tsa.shop.logout;

import javax.servlet.http.Cookie;

public interface LogoutService {
    void logout(Cookie tokenCookie);
}
