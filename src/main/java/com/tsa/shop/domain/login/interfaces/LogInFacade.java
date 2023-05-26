package com.tsa.shop.domain.login.interfaces;


import jakarta.servlet.http.Cookie;

import java.util.Map;

public interface LogInFacade {
    Cookie process(Map<String, String[]> parameters);
}
