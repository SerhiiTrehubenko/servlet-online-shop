package com.tsa.shop.login.interfaces;


import javax.servlet.http.Cookie;

import java.util.Map;

public interface LogInFacade {
    Cookie process(Map<String, String[]> parameters);
}
