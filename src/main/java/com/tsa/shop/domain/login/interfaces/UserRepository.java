package com.tsa.shop.domain.login.interfaces;

import com.tsa.shop.domain.login.impl.User;

public interface UserRepository {
    User getUser(String name);
    void addUser(User user);
}
